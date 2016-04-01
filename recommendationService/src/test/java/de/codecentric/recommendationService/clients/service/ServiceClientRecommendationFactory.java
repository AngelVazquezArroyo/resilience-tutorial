package de.codecentric.recommendationService.clients.service;

import de.codecentric.recommendationService.RecommendationConfiguration;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by afitz on 24.03.16.
 */
public class ServiceClientRecommendationFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClientRecommendationFactory.class);

    private DropwizardAppRule<RecommendationConfiguration> rule;

    public ServiceClientRecommendationFactory(DropwizardAppRule<RecommendationConfiguration> rule) {
        this.port = rule.getLocalPort();
        this.host = "localhost";
        this.portAdmin = rule.getAdminPort();
        this.rule = rule;
    }

    private String host;

    @Min(1)
    @Max(65535)
    private int port;

    @Min(1)
    @Max(65535)
    private int portAdmin;

    private String path = "/recommendation";

    public ServiceClient build(String name) throws ServiceClientException{

        CloseableHttpClient httpClient = HttpClients.createDefault();

        logger.info("build Service Client to access recommendationService");

        ServiceClient awesomeRecommendation = new ServiceClientRecommendation(host, port, portAdmin, path, httpClient);

        logger.info("build is " + awesomeRecommendation == null ? "error" : "success");

        return awesomeRecommendation;
    }
}
