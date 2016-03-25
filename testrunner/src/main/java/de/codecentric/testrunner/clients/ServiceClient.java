package de.codecentric.testrunner.clients;

import de.codecentric.testrunner.clients.service.ServiceHealthResult;
import de.codecentric.testrunner.clients.service.ServiceHealthStatusStructure;
import de.codecentric.testrunner.clients.service.ServiceHealthStatusCode;

/**
 * Created by afitz on 24.03.16.
 */
public interface ServiceClient {
    public ServiceHealthResult getHealthy(ServiceHealthStatusCode check) throws ServiceClientException;
    public ServiceHealthResult getHealthy() throws ServiceClientException;
}