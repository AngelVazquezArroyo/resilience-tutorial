package de.codecentric.recommendationService.clients.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ServiceClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * A client wrapping the actual access to the recommendation service.
 *
 * @author afitz
 */
public class ServiceClientRecommendation implements ServiceClient {
    private String host;
    private int port;
    private int portAdmin;
    private String path;
    private HttpClient client;

    private URI healthCheckUri = null;
    private HttpGet getHealth;
    private ObjectMapper mapper = new ObjectMapper();

    private ResponseHandler<ServiceHealthResult> responseHandlerHealthCheck;
    private ResponseHandler<Recommendation> responseHandlerGetRecommendation;

    public ServiceClientRecommendation(String host, int port, int portAdmin, String path,
                                       HttpClient client) throws ClientException {
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
            getHealth = new HttpGet(healthCheckUri);

            responseHandlerGetRecommendation = new ResponseHandler<Recommendation>() {
                public Recommendation handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    String entity = null;
                    if (response.getEntity() != null) {
                        entity = EntityUtils.toString(response.getEntity());
                    }

                    if (status != HttpStatus.SC_OK) {
                        throw new ClientProtocolException("Unexpected response status " + status);
                    }
                    if (entity == null) {
                        throw new ClientProtocolException("Empty response entity");
                    }

                    return mapper.readValue(entity, Recommendation.class);
                }
            };

            responseHandlerHealthCheck = new ResponseHandler<ServiceHealthResult>() {
                public ServiceHealthResult handleResponse(final HttpResponse response) throws
                        IOException {
                    int status = response.getStatusLine().getStatusCode();
                    String entity = null;
                    if (response.getEntity() != null) {
                        entity = EntityUtils.toString(response.getEntity());
                    }

                    ServiceHealthResult health = null;
                    if (status == HttpStatus.SC_OK) {
                        health = new ServiceHealthResult(status, "Service is healthy", entity);
                    } else if (status == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                        return new ServiceHealthResult(status, "Service is unhealthy", entity);
                    } else {
                        return new ServiceHealthResult(status, "Service is in unknown state",
                                entity);
                    }
                    return health;
                }
            };
        } catch (Exception e) {
            throw new ClientException("Unexpected problem in recommendation service client " +
                    "constructor (see embedded exception)", e);
        }
    }

    @Override
    public ServiceHealthResult getServiceHealth(ServiceHealthStatusCode check) throws
            ClientException {
        // Todo extract the requested health status
        return this.getServiceHealth();
    }

    @Override
    public ServiceHealthResult getServiceHealth() throws ClientException {
        try {
            return client.execute(getHealth, responseHandlerHealthCheck);
        } catch (IOException e) {
            throw new ClientException("Retrieving service health failed (see embedded exception)",
                    e);
        }
    }

    @Override
    public Recommendation getRecommendation(String user, String product) throws ClientException {
        URI serviceUri;
        HttpGet getRecommendation;
        Recommendation recommendation = null;

        try {
            serviceUri = new URIBuilder().setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath(this.path)
                    .setParameter("product", product)
                    .setParameter("user", user)
                    .build();
            getRecommendation = new HttpGet(serviceUri);
            recommendation = client.execute(getRecommendation, responseHandlerGetRecommendation);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Creating URI to retrieve recommendation failed (see " +
                    "embedded exception)", e);
        } catch (IOException e) {
            throw new ClientException("Retrieving recommendation failed (see embedded exception)",
                    e);
        }
        return recommendation;
    }
}
