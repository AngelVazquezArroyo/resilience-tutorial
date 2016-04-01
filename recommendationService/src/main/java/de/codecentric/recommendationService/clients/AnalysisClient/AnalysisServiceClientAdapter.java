package de.codecentric.recommendationService.clients.AnalysisClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
 * Created by afitz on 22.03.16.
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
    public Products executeGetProducts(String product) throws AnalysisServiceException{
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPort(port)
                    .setPath(path)
                    .setParameter("product", product)
                    .build();
        } catch (URISyntaxException e) {
            logger.error(e.toString());
            throw new AnalysisServiceException("Unknown Exception in Cosntructor: " + e.getMessage());
        }

        HttpGet get = new HttpGet(uri);
        get.addHeader("accept", "application/json");

        // Create a custom response handler
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };

        String responseBody = null;
        try {
            logger.info("get = " + uri.toString());
            responseBody = httpAnalysisService.execute(get, responseHandler);
            logger.info("response = " + responseBody);
        } catch (ClientProtocolException e){
            logger.error("ClientProtocolException"  + e.getMessage());
            throw new AnalysisServiceException("AnalysisServiceException: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException response AnaylisisClientAdapter: " + e.getMessage());
            throw new AnalysisServiceException("AnalysisService is not available: " + e.getMessage());
        }

        Products recommendProducts = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            recommendProducts = objectMapper.readValue(responseBody, Products.class);
        } catch (IOException e) {
            logger.error("IOException Mapper: " + e.getMessage());
            throw new AnalysisServiceException("AnalysisService is not available: " + e.getMessage());
        }

        return recommendProducts;
    }

    @Override
    public Boolean ping() {

        Boolean available = true;

        try {
            this.executeGetProducts("P00T");
        } catch (AnalysisServiceException e) {
            logger.error("ping get no response: " + e.getMessage());
            available = false;
        } catch (Exception e){
            logger.error("oh no, more exceptions" + e.getMessage());
        }
        return available;
    }
}
