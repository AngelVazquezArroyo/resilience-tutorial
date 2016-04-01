package de.codecentric.recommendationService.clients;

/**
 * Created by afitz on 24.03.16.
 */
public interface ImpostorClient {
    public void setConfig(ImpostorConfig config) throws ServiceClientException;
    public void executeCommand(ImpostorCommands command) throws ServiceClientException;
}
