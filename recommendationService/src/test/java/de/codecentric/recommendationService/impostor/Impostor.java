package de.codecentric.recommendationService.impostor;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A handling wrapper around an impostor instance.
 *
 * @author ufr
 */
public class Impostor {
    private static final String SCHEME = "http";
    private static final String CONFIG_PATH = "/config";
    private static final String COMMAND_PATH = "/command";
    private static final String HEALTH_PATH = "/health";

    private final String host;
    private final int port;
    private final Process process;
    private final HttpClient client;
    private final URI configUri;
    private final URI commandUri;
    private final URI healthUri;


    public Impostor(String host, int port, Process process) {
        this.host = host;
        this.port = port;
        this.process = process;
        this.client = HttpClients.createDefault();
        this.configUri = createURI(CONFIG_PATH);
        this.commandUri = createURI(COMMAND_PATH);
        this.healthUri = createURI(HEALTH_PATH);
    }

    private URI createURI(String path) {
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme(SCHEME)
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath(path)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Creating URI to access impostor failed (" + e
                    .getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return uri;
    }

    public void destroy() {
        process.destroy();
    }

    public void setConfig(ImpostorConfiguration config) {
        ImpostorResponse response = post(configUri, new StringEntity(config.get(),
                ContentType.APPLICATION_JSON));
        if (response.getStatus() != HttpStatus.SC_NO_CONTENT) {
            throw new ImpostorException("Setting configuration at impostor failed with status " +
                    response.getStatus() + ". Configuration was: " + config.get());
        }
    }

    public String executeCommand(ImpostorCommand command) throws ImpostorException {
        ImpostorResponse response = post(commandUri, new StringEntity(command.get(),
                ContentType.APPLICATION_JSON));
        if ((response.getStatus() != HttpStatus.SC_OK) &&
                (response.getStatus() != HttpStatus.SC_NO_CONTENT)) {
            throw new ImpostorException("Executing command at impostor failed with status " +
                    response.getStatus() + ". Command was: " + command.get());
        }
        return response.getBody();
    }

    public boolean isHealthy() throws ImpostorException {
        ImpostorResponse response = get(healthUri);
        return (response.getStatus() != HttpStatus.SC_OK);
    }

    private ImpostorResponse get(URI uri) {
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
            throw new ImpostorException("Problem accessing impostor with URI " + uri + " (" + e
                    .getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return new ImpostorResponse(status, body);
    }

    private ImpostorResponse post(URI uri, StringEntity entity) {
        HttpPost request;
        HttpResponse response;
        int status;
        String body = null;

        try {
            request = new HttpPost(uri);
            request.setEntity(entity);
            response = client.execute(request);
            status = response.getStatusLine().getStatusCode();
            if (response.getEntity() != null) {
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            throw new ImpostorException("Problem accessing impostor with URI " + uri + " (" + e
                    .getClass().getName() + ", " + e.getMessage() + ")", e);
        }
        return new ImpostorResponse(status, body);
    }
}

class ImpostorResponse {
    private int status;
    private String body;

    public ImpostorResponse(int status, String body) {
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
