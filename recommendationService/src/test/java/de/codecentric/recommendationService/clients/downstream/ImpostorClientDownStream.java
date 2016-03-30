package de.codecentric.recommendationService.clients.downstream;

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

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientDownStream implements ImpostorClient {

    private String host;
    private int port;
    private HttpClient client;

    public ImpostorClientDownStream(String host, int port, HttpClient client) {
        this.host = host;
        this.port = port;
        this.client = client;
    }

    @Override
    public void setConfig(ImpostorConfig config) {

        HttpPost downStreamConfigRequest = null;
        URI downStreamConfigURI;

        try {

//            System.out.println("setconfig to " + config.getJSon());

            downStreamConfigURI = new URIBuilder()
                    .setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath("/config")
                    .build();
            downStreamConfigRequest = new HttpPost(downStreamConfigURI);

            System.out.println("DownStreamConfig: " + config.toString());

            StringEntity requestJson = new StringEntity(config.getJSon(), ContentType.APPLICATION_JSON);
            downStreamConfigRequest.setEntity(requestJson);

            HttpResponse response = null;
            response = client.execute(downStreamConfigRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeCommand(ImpostorCommand command) {

    }
}


