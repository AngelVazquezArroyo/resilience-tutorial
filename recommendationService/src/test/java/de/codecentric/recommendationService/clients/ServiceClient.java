package de.codecentric.recommendationService.clients;

import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.service.ServiceHealthResult;
import de.codecentric.recommendationService.clients.service.ServiceHealthStatusCode;

/**
 * Interface that a client wrapping the actual access to the recommendation service must implement.
 *
 * @author afitz
 */
public interface ServiceClient{
    public Recommendation getRecommendation(String user, String product) throws ClientException;
    public ServiceHealthResult getServiceHealth(ServiceHealthStatusCode check) throws ClientException;
    public ServiceHealthResult getServiceHealth() throws ClientException;
}
