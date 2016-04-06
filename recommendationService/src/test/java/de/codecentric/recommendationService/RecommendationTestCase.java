package de.codecentric.recommendationService;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.config.LoadTestConfiguration;
import de.codecentric.recommendationService.config.TestConfiguration;
import de.codecentric.recommendationService.rules.ImpostorRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

/**
 * Created by afitz on 06.04.16.
 */
public class RecommendationTestCase{

    private static final Logger logger = LoggerFactory.getLogger(TestNormal.class);

    private static TestConfiguration config = new LoadTestConfiguration().get();

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
    public static void initialize() {

        logger.debug("-----------------------------------------------------------");
        logger.debug("initialize Clients");
        logger.debug("-----------------------------------------------------------");

        try {

            // get clients to all impostors
            impostorUpStreamClient = config.getUpStreamFactory().build(); //default config is ImpostorClientUpStreamConfig.NORMAL
            impostorDownStreamClient = config.getDownStreamFactory().build(); //default config is ImpostorClientDownStreamConfig.NORMAL

            // get clients to recommendation service
            recommendationServiceClient = new ServiceClientRecommendationFactory(SERVICE_RULE).build("recommendationServiceClient client");

        } catch (ServiceClientException e) {
            logger.error("Unexpected ServiceClientException during initialization: " + e.getMessage());
            fail();
        }

    }

    @AfterClass
    public static void tearDown() {
        // nothing to do at the moment!
    }

    // getter and setter methods

    public static ImpostorClient getImpostorUpStreamClient() {
        return impostorUpStreamClient;
    }

    public static void setImpostorUpStreamClient(ImpostorClient impostorUpStreamClient) {
        RecommendationTestCase.impostorUpStreamClient = impostorUpStreamClient;
    }

    public static ImpostorClient getImpostorDownStreamClient() {
        return impostorDownStreamClient;
    }

    public static void setImpostorDownStreamClient(ImpostorClient impostorDownStreamClient) {
        RecommendationTestCase.impostorDownStreamClient = impostorDownStreamClient;
    }

    public static ServiceClient getRecommendationServiceClient() {
        return recommendationServiceClient;
    }

    public static void setRecommendationServiceClient(ServiceClient recommendationServiceClient) {
        RecommendationTestCase.recommendationServiceClient = recommendationServiceClient;
    }
}
