package de.codecentric.recommendationService.clients.upstream;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ImpostorCommands;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import de.codecentric.recommendationService.clients.ServiceClientException;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientUpStream extends ImpostorClient {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClientUpStream.class);


    public ImpostorClientUpStream(String host, int port, HttpClient client) {
        super(host, port, client);
    }

    @Override
    public void setConfig(ImpostorConfig config) throws ServiceClientException {

        logger.debug("UpStreamConfig: " + config.toString());

        try {
            int status = this.sendToImpostor(new StringEntity(config.getJSon(), ContentType.APPLICATION_JSON));

            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_NO_CONTENT) {
                logger.debug("set config to: " + config.toString() + " was successfull");
            } else {
                throw new ServiceClientException("set config " + config.toString() + " to ImpostorRule was not successfull. Status: " + status);
            }
        } catch (ServiceClientException e) {
            throw e;
        }
    }

    @Override
    public void executeCommand(ImpostorCommands command) throws ServiceClientException {

        logger.debug("UpStreamCommand: " + command.toString());

        try {
            int status = sendToImpostor(new StringEntity(command.getJSon(), ContentType.APPLICATION_JSON));

            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_NO_CONTENT) {
                logger.debug("set config to " + command.toString() + " was successfull");
            } else {
                throw new ServiceClientException("send command " + command.toString() + " to DownStreamImpostor was not successfull. Status: " + status);
            }
        } catch (ServiceClientException e) {
            throw e;
        }

    }
}
