package de.codecentric.recommendationService.clients;

import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStream;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by afitz on 24.03.16.
 */
public abstract class ImpostorClient{

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClient.class);

    public abstract void setConfig(ImpostorConfig config) throws ServiceClientException;

    public abstract void executeCommand(ImpostorCommands command) throws ServiceClientException;

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
            e.printStackTrace();
        }
    }

    protected int sendToImpostor(StringEntity requestJson) {

        HttpPost impostorConfigRequest = null;
        int status = 999; // error!

        try {

            impostorConfigRequest = new HttpPost(impostorUri);

            impostorConfigRequest.setEntity(requestJson);

            HttpResponse response = client.execute(impostorConfigRequest);

            status = response.getStatusLine().getStatusCode();

        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException in sendToImpostor: " + e.getMessage());
        } catch (NoHttpResponseException e){
            logger.error("NoHttpResponseException in sendToImpostor: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException in sendToImpostor: " + e.getMessage());
        }
        return status;

    }

    protected Boolean ping(StringEntity requestJson) {
        return (this.sendToImpostor(requestJson) != 999 ? true: false);
    }
}
