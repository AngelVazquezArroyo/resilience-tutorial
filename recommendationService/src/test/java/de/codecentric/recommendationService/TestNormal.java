package de.codecentric.recommendationService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.config.LoadTestConfiguration;
import de.codecentric.recommendationService.config.TestConfiguration;
import de.codecentric.recommendationService.rules.ImpostorRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by afitz on 05.04.16.
 */
public class TestNormal {

    private static final Logger logger = LoggerFactory.getLogger(TestNormal.class);

    private static TestConfiguration config = new LoadTestConfiguration().get();
//    private static Logger rootlogger = config.getLogger().build();

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
            impostorUpStreamClient = config.getUpStreamFactory().build(); //with config.normal
            impostorDownStreamClient = config.getDownStreamFactory().build(); //with config.normal

            // get clients to recommendation service
            recommendationServiceClient = new ServiceClientRecommendationFactory(SERVICE_RULE).build("recommendationServiceClient client");

        } catch (ServiceClientException e) {
            logger.error("Unexpected ServiceClientException in initialize: " + e.getMessage());
        }

    }

    @Test //deilver an exsiting recommendation
    public void testNormal() {

        logger.debug("-----------------------------------------------------------");
        logger.debug("execute: " + name.getMethodName());
        logger.debug("-----------------------------------------------------------");

        try {

            impostorDownStreamClient.setConfig(ImpostorClientDownStreamConfig.NORMAL);

            //return existing product
            Recommendation recommendation = recommendationServiceClient.getRecommendation("U001", "P001");
            ArrayList<String> expectedProducts = new ArrayList<String>();
            expectedProducts.add("P002");
            assertArrayEquals("expected produt(s)", expectedProducts.toArray(), recommendation.getProducts().toArray());

            // return dedault product
            recommendation = recommendationServiceClient.getRecommendation("U001", "P00T");
            expectedProducts = new ArrayList<String>();
            expectedProducts.add("P001");
            assertArrayEquals("expected produt(s)", expectedProducts.toArray(), recommendation.getProducts().toArray());



        } catch (ServiceClientException e) {
            logger.error(e.getMessage());
            //tbd!: abort execution
        }

    }

    @AfterClass
    public static void resetImpostor() {
        logger.debug("-----------------------------------------------------------");
        logger.debug("resetImpostor");
        logger.debug("-----------------------------------------------------------");
        logger.debug("nothing to do at the moment!");
    }
}
