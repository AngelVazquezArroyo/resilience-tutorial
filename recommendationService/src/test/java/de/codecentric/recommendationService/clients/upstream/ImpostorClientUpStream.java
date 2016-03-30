package de.codecentric.recommendationService.clients.upstream;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ImpostorCommand;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientUpStream implements ImpostorClient {

    private String host;
    private int port;
    private HttpClient client;
    private URI upStreamConfigURI;

    public ImpostorClientUpStream(String host, int port, HttpClient client) {
        this.host = host;
        this.port = port;
        this.client = client;
        try {
            upStreamConfigURI = new URIBuilder()
                    .setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath("/config")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setConfig(ImpostorConfig config) {

        HttpPost upStreamConfigRequest = null;

        try {
            upStreamConfigRequest = new HttpPost(upStreamConfigURI);

            System.out.println("UpStreamConfig: " + config.toString());

            StringEntity requestJson = new StringEntity(config.getJSon(), ContentType.APPLICATION_JSON);
            upStreamConfigRequest.setEntity(requestJson);

            HttpResponse response = null;
            response = client.execute(upStreamConfigRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeCommand(ImpostorCommand command) {
// tbd!
    }
}
