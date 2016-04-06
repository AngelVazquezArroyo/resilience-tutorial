package de.codecentric.recommendationService;

import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.recommendationService.clients.service.ServiceHealthResult;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStreamCommands;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Created by afitz on 23.03.16.
 */
public class TestBulkheads extends RecommendationTestCase{

    private static final Logger logger = LoggerFactory.getLogger(TestBulkheads.class);

    @Test // test RecurringLatency
    public void testRecurringLatency() {

        ServiceHealthResult health;

        try {
            // configure the impostors tor test behavior
            getImpostorDownStreamClient().setConfig(ImpostorClientDownStreamConfig.RECURRING_LATENCY);
            getImpostorUpStreamClient().executeCommand(ImpostorClientUpStreamCommands.PRESSURE_ON);

            health = getRecommendationServiceClient().getHealthy();

//            assertEquals("Status must be: ", 200, health.getStatusCode());
            assertEquals("Status must be: ", 500, health.getStatusCode());

            // configure impostors to normal behavior
            getImpostorDownStreamClient().setConfig(ImpostorClientDownStreamConfig.NORMAL);
            getImpostorUpStreamClient().executeCommand(ImpostorClientUpStreamCommands.PRESSURE_OFF);

        } catch (ServiceClientException e) {
            logger.error("ServiceClientException in " + name.getMethodName() + ": " + e.getMessage());
            fail();
        } catch (Exception e) {
            logger.error(" Unknown Exception in " + name.getMethodName() + ": " + e.getMessage());
            fail();
        }

    }

}
