package de.codecentric.recommendationService.clients.AnalysisClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import de.codecentric.recommendationService.core.Products;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

/**
 * Concrete implementation of a product analysis client using a HTTP based analysis service.
 *
 * @author afitz
 */
public class AnalysisServiceClientAdapter implements AnalysisServiceClient {
    private String host = null;
    private int port = 0;
    private int portFailover = 0;
    private String path;
    private HttpClient analysisServiceClient;

    public AnalysisServiceClientAdapter(String host, int port, int portFailover, String path,
                                        HttpClient httpClient) {
        this.port = port;
        this.portFailover = portFailover;
        this.host = host;
        this.path = path;
        this.analysisServiceClient = httpClient;
    }

    // ************************************************************************************
    //
    // This method is one of the places where you should implement your changes.
    //
    // Usually, this method would not be factored out this way. It was just done for the
    // purpose of the resilience tutorial to better isolate the points of change.
    //
    // ************************************************************************************
    @Override
    public Products getCrossUpSellingProducts(String product) {
        String response;
        AnalysisServiceRequest request = new AnalysisServiceRequest(host, port, path,
                analysisServiceClient, product);

        try {
            response = request.execute();
        } catch (HystrixRuntimeException e) {
            if (request.isCircuitBreakerOpen()) {
                throw new WebApplicationException("Circuit breaker to analysis service tripped",
                        HttpStatus.SC_SERVICE_UNAVAILABLE);
            } else {
                response = "{}";
            }
        }

        Products cuProducts;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            cuProducts = objectMapper.readValue(response, Products.class);
            if (cuProducts.getProducts() == null) {
                cuProducts.setProducts(Collections.<String>emptyList());
            }
        } catch (IOException e) {
            cuProducts = new Products(Collections.<String>emptyList());
        }

        return cuProducts;
    }

    @Override
    public boolean ping() {
        boolean available = true;
        try {
            this.getCrossUpSellingProducts("P00T");
        } catch (AnalysisServiceException e) {
            available = false;
        }
        return available;
    }
}


class AnalysisServiceRequest extends HystrixCommand<String> {
    private static final String GROUP = "AnalysisService";
    private static final int TIMEOUT = 200;
    private static final int THRESHOLD = 1;
    private static final int WINDOW_SIZE = 1000;
    private static final int WINDOW_BUCKETS = 1;


    private String host;
    private int port;
    private String path;
    private HttpClient client;
    private String product;

    AnalysisServiceRequest(String host, int port, String path, HttpClient client, String product) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(TIMEOUT)
                        .withCircuitBreakerRequestVolumeThreshold(THRESHOLD)
                        .withMetricsRollingStatisticalWindowInMilliseconds(WINDOW_SIZE)
                        .withMetricsRollingStatisticalWindowBuckets(WINDOW_BUCKETS)
                        .withMetricsRollingPercentileEnabled(false)));
        this.port = port;
        this.host = host;
        this.path = path;
        this.client = client;
        this.product = product;
    }

    @Override
    public String run() throws Exception {
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPort(port)
                    .setPath(path)
                    .setParameter("product", product)
                    .build();
        } catch (URISyntaxException e) {
            // Turn into RuntimeException for appropriate handling via exception mapper
            throw new RuntimeException("Unexpected Exception while creating URI to access" +
                    " analysis service", e);
        }

        String body;

        HttpGet get = new HttpGet(uri);
        get.addHeader("accept", "application/json");
        HttpResponse response = client.execute(get);

        int status = response.getStatusLine().getStatusCode();
        if (status == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            body = (entity != null) ? EntityUtils.toString(entity) : "";
        } else if (status == HttpStatus.SC_NOT_FOUND) {
            body = "{}";
        } else {
            throw new WebApplicationException("Unexpected response status from analysis " +
                    "service: " + status,
                    HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        return body;
    }
}
