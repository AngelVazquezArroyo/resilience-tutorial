package de.codecentric.recommendationService.clients.upstream;

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
 * A client wrapping the actual access to the upstream impostor.
 *
 * @author afitz
 */
public class ImpostorClientUpstream extends ImpostorClient {
    public ImpostorClientUpstream(String host, int port, HttpClient client) {
        super(host, port, client);
    }

    @Override
    public void setConfig(ImpostorConfig config) {
        ImpostorResult result = this.sendToImpostor(new StringEntity(config.getJSon(),
                ContentType.APPLICATION_JSON));
        if (result.getStatus() != HttpStatus.SC_NO_CONTENT) {
            throw new ClientException("Setting configuration at upstream impostor failed with " +
                    "status " + result.getStatus() + ". Configuration was: " + config.getJSon());
        }
    }

    @Override
    public String executeCommand(ImpostorCommands command) throws ClientException {
        ImpostorResult result = sendToImpostor(new StringEntity(command.getJSon(),
                ContentType.APPLICATION_JSON));
        if ((result.getStatus() != HttpStatus.SC_OK) &&
                (result.getStatus() != HttpStatus.SC_NO_CONTENT)) {
            throw new ClientException("Executing command at upstream impostor failed with status " +
                    "" + result.getStatus() + ". Command was: " + command.getJSon());
        }
        return result.getBody();
    }
}
