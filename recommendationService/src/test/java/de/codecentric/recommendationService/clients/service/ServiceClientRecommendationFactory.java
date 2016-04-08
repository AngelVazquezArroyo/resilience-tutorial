package de.codecentric.recommendationService.clients.service;

import de.codecentric.recommendationService.RecommendationConfiguration;
import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ServiceClient;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ServiceClientRecommendationFactory {
    private static final String path = "/recommendation";

    private String host;

    @Min(1)
    @Max(65535)
    private int port;

    @Min(1)
    @Max(65535)
    private int portAdmin;

    public ServiceClientRecommendationFactory(DropwizardAppRule<RecommendationConfiguration> rule) {
        this.port = rule.getLocalPort();
        this.host = "localhost";
        this.portAdmin = rule.getAdminPort();
    }

    public ServiceClient build(String name) throws ClientException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ServiceClient awesomeRecommendation = new ServiceClientRecommendation(host, port,
                portAdmin, path, httpClient);
        return awesomeRecommendation;
    }
}
