package de.codecentric.recommendationService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;

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
 * Created by afitz on 24.03.16.
 */
public class ServiceClientRecommendation implements ServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClientRecommendation.class);

    private String host;
    private int port;
    private int portAdmin;
    private String path;
    private HttpClient client;

    private URI healthCheckUri = null;
//    private URI serviceUri = null;

    private HttpGet getHealthy;

    private ObjectMapper mapper = new ObjectMapper();

    private ResponseHandler<ServiceHealthResult> responseHandlerHeathCheck;
    private ResponseHandler<Recommendation> responseHandlerGetRecommendation;

    public ServiceClientRecommendation(String host, int port, int portAdmin, String path, HttpClient client) throws ServiceClientException {

        this.host = host;
        this.port = port;
        this.portAdmin = portAdmin;
        this.path = path;

        this.client = client;

        try {
            healthCheckUri = new URIBuilder().setScheme("http")
                    .setHost(host)
                    .setPort(portAdmin)
                    .setPath("/healthcheck")
                    .build();

            logger.info("healthCheckUri: " + healthCheckUri.toString());

            getHealthy = new HttpGet(healthCheckUri);

            // Create a custom response handler for HealthChecks
            responseHandlerGetRecommendation = new ResponseHandler<Recommendation>() {

                public Recommendation handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {

                    int status = response.getStatusLine().getStatusCode();
                    String entityString;
                    Recommendation recommendation = null;

                    entityString = (response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "no health checks");

                    if (status == HttpStatus.SC_OK) {
                        recommendation = mapper.readValue(entityString, Recommendation.class);
                    } else {
                        new ClientProtocolException("Unexpected response status: " + status + " - " + entityString);
                    }
                    return recommendation;
                }
            };

            // Create a custom response handler for HealthChecks
            responseHandlerHeathCheck = new ResponseHandler<ServiceHealthResult>() {

                public ServiceHealthResult handleResponse(final HttpResponse response) throws IOException {

                    int status = response.getStatusLine().getStatusCode();
                    String message;
                    String entityString;

                    entityString = (response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "no products");

                    if (status == HttpStatus.SC_OK) {
                        return new ServiceHealthResult(status, "service seems to be healthy", entityString);
                    } else {
                        return new ServiceHealthResult(status, "Unexpected response status: " + status, entityString);
                    }
                }
            };

        } catch (Exception e) {
            throw new ServiceClientException("ServiceClient UnknownException in Constructor: " + e.getMessage());
        }

    }

    @Override
    public ServiceHealthResult getHealthy(ServiceHealthStatusCode check) throws ServiceClientException {
        // tbd!: extract the requestet health status
        return this.getHealthy();
    }

    @Override
    public ServiceHealthResult getHealthy() throws ServiceClientException {

        try {
            return client.execute(getHealthy, responseHandlerHeathCheck);
        } catch (ClientProtocolException e) {
            throw new ServiceClientException("ServiceClient ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            throw new ServiceClientException("ServiceClient IOException: " + e.getMessage());
        } catch (Exception e) {
            throw new ServiceClientException("ServiceClient UnknownException: " + e.getMessage());
        }
    }

    @Override
    public Recommendation getRecommendation(String user, String product) throws ServiceClientException {

        URI serviceUri = null;
        HttpGet getRecommendation = null;
        Recommendation recommendation = null;

        try {
            serviceUri = new URIBuilder().setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath(this.path)
                    .setParameter("product", product)
                    .setParameter("user", user)
                    .build();

            logger.debug("serviceUri: " + serviceUri.toString());

            getRecommendation = new HttpGet(serviceUri);

            recommendation = client.execute(getRecommendation, responseHandlerGetRecommendation);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recommendation;
    }
}
