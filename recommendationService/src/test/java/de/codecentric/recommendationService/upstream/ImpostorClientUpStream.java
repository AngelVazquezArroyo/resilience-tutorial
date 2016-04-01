package de.codecentric.recommendationService.upstream;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ImpostorCommands;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientUpStream implements ImpostorClient {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClientUpStream.class);

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

        logger.info("UpStreamConfig: " + config.toString());

        this.sendToImpostor(new StringEntity(config.getJSon(), ContentType.APPLICATION_JSON));

    }

    @Override
    public void executeCommand(ImpostorCommands command) {

        logger.info("UpStreamCommand: " + command.toString());

        this.sendToImpostor(new StringEntity(command.getJSon(), ContentType.APPLICATION_JSON));

    }

    private void sendToImpostor(StringEntity requestJson) {
        try {
            HttpPost upStreamConfigRequest = new HttpPost(upStreamConfigURI);

            upStreamConfigRequest.setEntity(requestJson);

            HttpResponse response = client.execute(upStreamConfigRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
