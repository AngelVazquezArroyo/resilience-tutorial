package de.codecentric.recommendationService.clients.AnalysisClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.recommendationService.core.Products;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Concrete implementation of a product analysis client using a HTTP based analysis service.
 *
 * @author afitz
 */
public class AnalysisServiceClientAdapter implements AnalysisServiceClient {
    private static final long TIMEOUT = 200L;

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
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            response = accessAnalysisService(executor, request);
        } catch (TimeoutException e) {
            try {
                response = accessAnalysisService(executor, request);
            } catch (TimeoutException e1) {
                throw new AnalysisServiceException("Did not get response from analysis service in" +
                        " time");
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

    private String accessAnalysisService(ExecutorService executor,
                                         AnalysisServiceRequest request) throws TimeoutException {
        String response;
        Future<String> responseFuture = null;

        try {
            responseFuture = executor.submit(request);
            response = responseFuture.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | CancellationException e) {
            throw new AnalysisServiceException("Unexpected exception (" + e.getClass()
                    .getName() + ", " + e.getMessage() + ")", e);
        } catch (ExecutionException e) {
            Throwable c = e.getCause();
            if (c instanceof WebApplicationException) {
                throw (WebApplicationException)c;
            }
            throw new AnalysisServiceException("Accessing analysis service failed (" + c.getClass()
                    .getName() + ", " + c.getMessage() + ")", c);
        } finally {
            if (responseFuture != null) {
                responseFuture.cancel(true);
            }
        }

        return response;
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


class AnalysisServiceRequest implements Callable<String> {
    private String host;
    private int port;
    private String path;
    private HttpClient client;
    private String product;

    AnalysisServiceRequest(String host, int port, String path, HttpClient client, String product) {
        this.port = port;
        this.host = host;
        this.path = path;
        this.client = client;
        this.product = product;
    }

    @Override
    public String call() throws Exception {
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

        HttpGet get = new HttpGet(uri);
        get.addHeader("accept", "application/json");

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response) throws IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : "";
                } else if (status == HttpStatus.SC_NOT_FOUND) {
                    return "{}";
                } else {
                    throw new WebApplicationException("Unexpected response status from analysis " +
                            "service: " + status,
                            HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
            }
        };

        String responseBody;
        responseBody = client.execute(get, responseHandler);
        return responseBody;
    }
}