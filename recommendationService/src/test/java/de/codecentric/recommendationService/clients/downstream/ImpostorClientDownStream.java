package de.codecentric.recommendationService.clients.downstream;

import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ImpostorCommands;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import de.codecentric.recommendationService.clients.ImpostorResult;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * A client wrapping the actual access to the downstream impostor.
 *
 * @author afitz
 */
public class ImpostorClientDownstream extends ImpostorClient {
    public ImpostorClientDownstream(String host, int port, HttpClient client) {
        super(host, port, client);
    }

    @Override
    public void setConfig(ImpostorConfig config) {
        ImpostorResult result = this.sendToImpostor(new StringEntity(config.getJSon(),
                ContentType.APPLICATION_JSON));
        if (result.getStatus() != HttpStatus.SC_NO_CONTENT) {
            throw new ClientException("Setting configuration at downstream impostor failed " +
                    "with status " + result.getStatus() + ". Configuration was: " + config
                    .getJSon());
        }
    }

    @Override
    public String executeCommand(ImpostorCommands command) throws ClientException {
        throw new ClientException("Downstream client cannot execute commands");
    }
}
