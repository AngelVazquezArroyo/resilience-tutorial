package de.codecentric.recommendationService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.recommendationService.api.Recommendation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A handling wrapper around a recommendation service instance.
 *
 * @author ufr
 */
public class Service {
    private static final String SCHEME = "http";
    private static final String SERVICE_PATH = "/recommendation";
    private static final String HEALTH_PATH = "/healthcheck";
    private static final String USER = "user";
    private static final String PRODUCT = "product";

    private final String host;
    private final int port;
    private final HttpClient client;
    private final URI healthUri;


    public Service(String host, int port, int adminPort) {
        this.host = host;
        this.port = port;
        this.client = HttpClients.createDefault();
        this.healthUri = createURIWithParameter(adminPort, HEALTH_PATH);
    }

    public Recommendation getRecommendation(String user, String product) throws ServiceException {
        URI uri;
        Recommendation recommendation;

        uri = createURIWithParameter(port, SERVICE_PATH, USER, user, PRODUCT, product);
        ServiceResponse response = get(uri);
        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new ServiceException("Retrieving recommendation from service failed with " +
                    "status " + response.getStatus() + ". URI was: " + uri.toString());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            recommendation = mapper.readValue(response.getBody(), Recommendation.class);
        } catch (IOException e) {
            throw new ServiceException("Parsing response entity \"" + response.getBody() + "\" " +
                    "failed (" + e.getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return recommendation;
    }

    public Recommendation getRecommendation(String query) throws ServiceException {
        URI uri;
        Recommendation recommendation;

        uri = createURIWithQuery(port, SERVICE_PATH, query);
        ServiceResponse response = get(uri);
        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new ServiceException("Retrieving recommendation from service failed with " +
                    "status " + response.getStatus() + ". URI was: " + uri.toString());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            recommendation = mapper.readValue(response.getBody(), Recommendation.class);
        } catch (IOException e) {
            throw new ServiceException("Parsing response entity \"" + response.getBody() + "\" " +
                    "failed (" + e.getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return recommendation;
    }

    public boolean isHealthy() throws ServiceException {
        ServiceResponse response = get(healthUri);
        return (response.getStatus() != HttpStatus.SC_OK);
    }

    private ServiceResponse get(URI uri) {
        HttpGet request;
        HttpResponse response;
        int status;
        String body = null;

        try {
            request = new HttpGet(uri);
            response = client.execute(request);
            status = response.getStatusLine().getStatusCode();
            if (response.getEntity() != null) {
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            throw new ServiceException("Problem accessing service with URI " + uri + " (" + e
                    .getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return new ServiceResponse(status, body);
    }

    private URI createURIWithParameter(int port, String path, String... kv) {
        URI uri;
        try {
            URIBuilder builder = new URIBuilder()
                    .setScheme(SCHEME)
                    .setHost(this.host)
                    .setPort(port)
                    .setPath(path);
            int s = kv.length;
            int i = 0;
            while (i < s) {
                String k = kv[i];
                String v = (++i < s) ? kv[i] : "";
                builder.addParameter(k, v);
                i++;
            }
            uri = builder.build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Creating URI to access service failed (" + e
                    .getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return uri;
    }

    private URI createURIWithQuery(int port, String path, String query) {
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme(SCHEME)
                    .setHost(this.host)
                    .setPort(port)
                    .setPath(path)
                    .setCustomQuery(query)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Creating URI to access service failed (" + e
                    .getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return uri;
    }
}

class ServiceResponse {
    private int status;
    private String body;

    public ServiceResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
