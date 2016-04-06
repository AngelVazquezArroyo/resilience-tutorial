package de.codecentric.recommendationService.clients.AnalysisClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.recommendationService.core.Products;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    @Override
    public Products getCrossUpSellingProducts(String product) throws AnalysisServiceException {
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
            throw new AnalysisServiceException("Unexpected Exception while creating URI to access" +
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
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };

        String responseBody;
        try {
            logger.info("get = " + uri.toString());
            responseBody = httpAnalysisService.execute(get, responseHandler);
            logger.info("response = " + responseBody);
        } catch (IOException e) {
            throw new AnalysisServiceException("Unexpected problem encountered while accessing " +
                    "analysis service", e);
        }

        Products cuProducts;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            cuProducts = objectMapper.readValue(responseBody, Products.class);
        } catch (IOException e) {
            throw new AnalysisServiceException("Unexpected problem while parsing JSON response " +
                    "from analysis service", e);
        }

        return cuProducts;
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
