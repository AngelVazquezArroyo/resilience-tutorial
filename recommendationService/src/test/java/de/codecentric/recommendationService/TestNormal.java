package de.codecentric.recommendationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.rules.TestConfiguration;
import de.codecentric.recommendationService.rules.ImpostorRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by afitz on 05.04.16.
 */
public class TestNormal {
    private static final Logger logger = LoggerFactory.getLogger(TestBulkheads.class);

    private static TestConfiguration config = new LoadTestConfiguration().build();

    private static ImpostorClient impostorUpStreamClient = null;
    private static ImpostorClient impostorDownStreamClient = null;
    private static ServiceClient recommendationServiceClient = null;

    @Rule
    public TestName name = new TestName();

    // start and teardown the recommendationServiceClient prior to any tests running and stop it again when they have completed
    @ClassRule
    public static final DropwizardAppRule<RecommendationConfiguration> SERVICE_RULE =
            new DropwizardAppRule<RecommendationConfiguration>(RecommendationService.class, ResourceHelpers.resourceFilePath("recommendationServiceConfiguration.yml"));

    // start and teardown the upStreamImpostor and the downStreamImpostor
    @ClassRule
    public static final ImpostorRule IMPOSTOR_RULE = new ImpostorRule(config);

    @BeforeClass
    public static void initializeImpostor() throws IOException {

        logger.info("-----------------------------------------------------------");
        logger.info("initialize Clients");
        logger.info("-----------------------------------------------------------");

        try {

            // build clients to all impostors
            impostorUpStreamClient = config.getUpStreamFactory().build(); //with config.normal
            impostorDownStreamClient = config.getDownStreamFactory().build(); //with config.normal

            // build clients to recommendation service
            recommendationServiceClient = new ServiceClientRecommendationFactory(SERVICE_RULE).build("recommendationServiceClient client");

        } catch (ServiceClientException e) {
            logger.error("Unexpected ServiceClientException: " + e.getMessage());
//            org.junit.Assume.assumeNoException(impostorUpStreamClient.ping());
        }

    }

    @Test //deilver an exsiting recommendation
    public void testNormal() {

        logger.info("-----------------------------------------------------------");
        logger.info("execute: " + name.getMethodName());
        logger.info("-----------------------------------------------------------");

        try {

            impostorDownStreamClient.setConfig(ImpostorClientDownStreamConfig.NORMAL);

            Recommendation recommendation = recommendationServiceClient.getRecommendation("U001", "P001");

            ObjectMapper mapper = new ObjectMapper();
            String productsJson = null;

            //tbd!: check ArrayList not the String.
            productsJson = mapper.writeValueAsString(recommendation.getProducts());

            logger.debug(productsJson);

            assertEquals("recommendation product(s) must be: ", "[\"P002\"]", productsJson);

        } catch (ServiceClientException e) {
            logger.error(e.getMessage());
            //tbd!: abort execution
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void resetImpostor() {
        logger.info("-----------------------------------------------------------");
        logger.info("resetImpostor");
        logger.info("-----------------------------------------------------------");
        logger.info("nothing to do at the moment!");
    }
}
