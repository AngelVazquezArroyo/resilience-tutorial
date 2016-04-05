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
import de.codecentric.recommendationService.clients.service.ServiceHealthResult;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStreamCommands;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStreamConfig;

import static org.junit.Assert.assertEquals;

import de.codecentric.recommendationService.rules.TestConfiguration;
import de.codecentric.recommendationService.processes.impostorProcess;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * Created by afitz on 23.03.16.
 */
public class TestBulkheads {

    private static final Logger logger = LoggerFactory.getLogger(TestBulkheads.class);

    private static ImpostorClient impostorUpStream = null;
    private static ImpostorClient impostorDownStream = null;
    private static ServiceClient recommendationService = null;

    private static impostorProcess downStreamProcess = null;
    private static impostorProcess upStreamProcess = null;

    //    start the recommendationService prior to any tests running and stop it again when they have completed
    @ClassRule
    public static final DropwizardAppRule<RecommendationConfiguration> RULE =
            new DropwizardAppRule<RecommendationConfiguration>(RecommendationService.class, ResourceHelpers.resourceFilePath("recommendationServiceConfiguration.yml"));

    @BeforeClass
    @Ignore
    public static void initializeImpostor() throws IOException {

        logger.info("-----------------------------------------------------------");
        logger.info("initializeImpostor");
        logger.info("-----------------------------------------------------------");

        // load TestConfiguration
        File yml = new File("./src/test/resources/testConfiguration.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestConfiguration config = mapper.readValue(yml, TestConfiguration.class);

        downStreamProcess = new impostorProcess();
        downStreamProcess.startImpostorProcess(config.getDownStreamFactory().getHost(), config.getDownStreamFactory().getPort());

        upStreamProcess = new impostorProcess();
        upStreamProcess.startImpostorProcess(config.getUpStreamFactory().getHost(), config.getUpStreamFactory().getPort());

        try {
            // build clients to all impostors
            impostorUpStream = config.getUpStreamFactory().build(); //with config.normal
            impostorDownStream = config.getDownStreamFactory().build(); //with config.normal

            // build clients to recommendation service
            recommendationService = new ServiceClientRecommendationFactory(RULE).build("recommendationService client");

        } catch (ServiceClientException e) {
            logger.error(e.getMessage());
            //tbd!: abort execution
        }
    }

    @Test //deilver an exsiting recommendation
    @Ignore
    public void testNormal() {

        logger.info("-----------------------------------------------------------");
        logger.info("testNormal");
        logger.info("-----------------------------------------------------------");

        try {

            impostorDownStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);

            Recommendation recommendation = recommendationService.getRecommendation("U001", "P001");

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

    @Test // test RecurringLatency
    @Ignore
    public void testRecurringLatency() {

        logger.info("-----------------------------------------------------------");
        logger.info("testRecurringLatency");
        logger.info("-----------------------------------------------------------");


        ServiceHealthResult health = null;

        try {
            impostorDownStream.setConfig(ImpostorClientDownStreamConfig.RECURRINGLATENCY);

            impostorUpStream.executeCommand(ImpostorClientUpStreamCommands.PRESSURE_ON);

            health = recommendationService.getHealthy();

            logger.info("Status: " + health.getStatusCode());
            logger.info("Message: " + health.getMessage());
            logger.info("Entity: " + health.getEntity());

            impostorDownStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);

            impostorUpStream.executeCommand(ImpostorClientUpStreamCommands.PRESSURE_OFF);

        } catch (ServiceClientException e) {
            logger.error(e.getMessage());
            //tbd!: abort execution
        } catch (Exception e) {
            logger.error("Message: " + e.getMessage());
        }

        assertEquals("Status must be: ", 200, health.getStatusCode());

    }

    @AfterClass
    public static void resetImpostor() {
        logger.info("-----------------------------------------------------------");
        logger.info("resetImpostor");
        logger.info("-----------------------------------------------------------");

        // set for all Impostor' the config to NORMAL
        try {
            impostorDownStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);
            impostorUpStream.setConfig(ImpostorClientUpStreamConfig.NORMAL);

            downStreamProcess.stopImpostorProcess();
            upStreamProcess.stopImpostorProcess();

            logger.info("Process: " + downStreamProcess.getProcessId());
        } catch (ServiceClientException e) {
            logger.error(e.getMessage());
            //tbd!: abort execution
        }
    }

}
