package de.codecentric.recommendationService;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.clients.service.ServiceHealthResult;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStreamCommands;

import static org.junit.Assert.assertEquals;

import de.codecentric.recommendationService.config.LoadTestConfiguration;
import de.codecentric.recommendationService.config.TestConfiguration;
import de.codecentric.recommendationService.rules.ImpostorRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by afitz on 23.03.16.
 */
public class TestBulkheads {

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

    @Ignore
    @BeforeClass
    public static void initialize() {

        logger.debug("-----------------------------------------------------------");
        logger.debug("initialize Clients");
        logger.debug("-----------------------------------------------------------");

        try {

            // get clients to all impostors
            impostorUpStreamClient = config.getUpStreamFactory().build(); //with config.normal
            impostorDownStreamClient = config.getDownStreamFactory().build(); //with config.normal

            // get clients to recommendation service
            recommendationServiceClient = new ServiceClientRecommendationFactory(SERVICE_RULE).build("recommendationServiceClient client");

        } catch (ServiceClientException e) {
            logger.error("Unexpected ServiceClientException: " + e.getMessage());
        }

    }


    @Test // test RecurringLatency
    public void testRecurringLatency() {

        logger.debug("-----------------------------------------------------------");
        logger.debug("testRecurringLatency");
        logger.debug("-----------------------------------------------------------");


        ServiceHealthResult health = null;

        try {
//            impostorDownStreamClient.setConfig(ImpostorClientDownStreamConfig.RECURRINGLATENCY);

//            impostorUpStreamClient.executeCommand(ImpostorClientUpStreamCommands.PRESSURE_ON);

            health = recommendationServiceClient.getHealthy();

            logger.debug("Status: " + health.getStatusCode());
            logger.debug("Message: " + health.getMessage());
            logger.debug("Entity: " + health.getEntity());

            impostorDownStreamClient.setConfig(ImpostorClientDownStreamConfig.NORMAL);

            impostorUpStreamClient.executeCommand(ImpostorClientUpStreamCommands.PRESSURE_OFF);

        } catch (ServiceClientException e) {
            logger.error("ServiceClientException in " + name.getMethodName() + ": " + e.getMessage());
            //tbd!: abort execution
        } catch (Exception e) {
            logger.error(" Unknown Exception in " + name.getMethodName() + ": " + e.getMessage());
        }

        assertEquals("Status must be: ", 200, health.getStatusCode());

    }

    @AfterClass
    public static void resetImpostor() {
        logger.debug("-----------------------------------------------------------");
        logger.debug("resetImpostor");
        logger.debug("-----------------------------------------------------------");
        logger.debug("nothing to do at the moment!");
    }

}
