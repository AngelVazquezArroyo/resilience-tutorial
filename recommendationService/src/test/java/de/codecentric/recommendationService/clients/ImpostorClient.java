package de.codecentric.recommendationService.clients;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Base class of a client wrapping the actual access to the downstream impostor.
 *
 * @author afitz
 */
public abstract class ImpostorClient {
    private String host;
    private int port;
    private HttpClient client;
    private URI impostorUri;

    public ImpostorClient(String host, int port, HttpClient client) {
        this.host = host;
        this.port = port;
        this.client = client;
        try {
            impostorUri = new URIBuilder()
                    .setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath("/config")
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Creating URI to access impostor failed (see embedded" +
                    " exception)", e);
        }
    }

    protected ImpostorResult sendToImpostor(StringEntity requestJson) throws ClientException {
        HttpPost request = null;
        HttpResponse response = null;
        int status = 0;
        String body = null;

        try {
            request = new HttpPost(impostorUri);
            request.setEntity(requestJson);
            response = client.execute(request);
            status = response.getStatusLine().getStatusCode();
            if (response.getEntity() != null) {
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            throw new ClientException("Problem accessing impostor (" + e.getClass().getName() +
                    ", " + e.getMessage() + ")", e);
        }
        return new ImpostorResult(status, body);
    }

    public abstract void setConfig(ImpostorConfig config) throws ClientException;

    public abstract String executeCommand(ImpostorCommands command) throws ClientException;
}
