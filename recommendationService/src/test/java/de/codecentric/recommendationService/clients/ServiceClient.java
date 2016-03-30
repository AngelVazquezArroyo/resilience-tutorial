package de.codecentric.recommendationService.clients;

import de.codecentric.recommendationService.clients.service.ServiceHealthResult;
import de.codecentric.recommendationService.clients.service.ServiceHealthStatusStructure;
import de.codecentric.recommendationService.clients.service.ServiceHealthStatusCode;

/**
 * Created by afitz on 24.03.16.
 */
public interface ServiceClient {
    public ServiceHealthResult getHealthy(ServiceHealthStatusCode check) throws ServiceClientException;
    public ServiceHealthResult getHealthy() throws ServiceClientException;
}
