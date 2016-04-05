package de.codecentric.recommendationService.clients.downstream;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ImpostorCommands;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import de.codecentric.recommendationService.clients.ServiceClientException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientDownStream extends ImpostorClient {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClientDownStream.class);


    public ImpostorClientDownStream(String host, int port, HttpClient client) {
        super(host, port, client);
    }

    @Override
    public void setConfig(ImpostorConfig config) throws ServiceClientException{

        logger.info("DownStreamConfig: " + config.toString());

        int status = this.sendToImpostor(new StringEntity(config.getJSon(), ContentType.APPLICATION_JSON));

        if (status == HttpStatus.SC_OK || status == HttpStatus.SC_NO_CONTENT) {
            logger.info("set config to " + config.toString() + " was successfull");
        } else {
            throw new ServiceClientException("set config " + config.toString() + " to DownStreamImpostor was not successfull. Status: " + status);
        }
    }

    @Override
    public void executeCommand(ImpostorCommands command) {
        // no commands at the moment
        logger.warn("At the moment, there are no commands fpr Impostor DownStream!");
    }
}

