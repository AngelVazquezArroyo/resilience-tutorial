package de.codecentric.recommendationService.clients;

import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.service.ServiceHealthResult;
import de.codecentric.recommendationService.service.ServiceHealthStatusCode;

/**
 * Created by afitz on 24.03.16.
 */
public interface ServiceClient{
    public ServiceHealthResult getHealthy(ServiceHealthStatusCode check) throws ServiceClientException;
    public ServiceHealthResult getHealthy() throws ServiceClientException;
    public Recommendation getRecommendation(String user, String product) throws ServiceClientException;
}
