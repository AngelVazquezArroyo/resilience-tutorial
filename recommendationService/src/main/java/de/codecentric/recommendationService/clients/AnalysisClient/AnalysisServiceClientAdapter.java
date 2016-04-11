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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceClientAdapter.class);

    private String host = null;
    private int port = 0;
    private String path;
    HttpClient httpAnalysisService;

    public AnalysisServiceClientAdapter(String host, int port, String path, HttpClient httpClient) {
        this.port = port;
        this.host = host;
        this.path = path;
        this.httpAnalysisService = httpClient;
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
        String responseBody;

        try {
            responseBody = accessAnalysisService(product);
        } catch (IOException e) {
            // Turn into runtime exception for handling by exception mapper
            throw new RuntimeException(e);
        }

        Products cuProducts;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            cuProducts = objectMapper.readValue(responseBody, Products.class);
        } catch (IOException e) {
            cuProducts = new Products(Collections.<String>emptyList());
        }

        return cuProducts;
    }

    private String accessAnalysisService(String product) throws
            IOException {

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
        logger.debug("GET request to analysis service: " + uri.toString());
        responseBody = httpAnalysisService.execute(get, responseHandler);
        logger.debug("GET response from analysis service: " + responseBody);
        return responseBody;
    }

    @Override
    public boolean ping() {
        boolean available = true;
        try {
            this.getCrossUpSellingProducts("P00T");
        } catch (AnalysisServiceException e) {
            logger.info("Analysis service is not accessible. Error message: " + e.getMessage());
            available = false;
        }
        return available;
    }
}
