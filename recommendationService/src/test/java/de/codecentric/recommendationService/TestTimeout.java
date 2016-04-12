package de.codecentric.recommendationService;

import de.codecentric.recommendationService.impostor.Impostor;
import de.codecentric.recommendationService.impostor.ImpostorConfiguration;
import de.codecentric.recommendationService.service.Service;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;


/**
 * Test suite with test cases for the "timeout" section of the resilience
 * tutorial.
 *
 * @author afitz
 */
public class TestTimeout {
    private static final int BASE_PORT = 8520;

    private static final int ANALYSIS_SERVICE_PORT = BASE_PORT;
    private static final int RECOMMENDATION_SERVICE_PORT = BASE_PORT + 1;
    private static final int RECOMMENDATION_SERVICE_ADMIN_PORT = BASE_PORT + 2;

    private static final long ACCEPTABLE_TIME_MILLIS = 250L;

    private static Impostor analysisService;
    private static Service recommendationService;

    @ClassRule
    public static final DropwizardAppRule<RecommendationConfiguration> SERVICE_RULE =
            new DropwizardAppRule<>(RecommendationService.class,
                    TestHelper.createServiceConfiguration(RECOMMENDATION_SERVICE_PORT,
                            RECOMMENDATION_SERVICE_ADMIN_PORT, ANALYSIS_SERVICE_PORT));

    @BeforeClass
    public static void initialize() throws IOException {
        analysisService = TestHelper.createImpostor(ANALYSIS_SERVICE_PORT,
                ImpostorConfiguration.DownstreamOneTimeLatency);
//                ImpostorConfiguration.DownstreamNormal);
        recommendationService = TestHelper.createService(RECOMMENDATION_SERVICE_PORT,
                RECOMMENDATION_SERVICE_ADMIN_PORT);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        analysisService.destroy();
    }

    @Test
    public void shouldHandleTimeout() {
        // Do a warm-up request and discard results as it will take a lot longer than expected
        // due to connection setup and initializations in the services and communication channels
        // involved.
        recommendationService.getRecommendation("U001", "P001");

        long s = System.currentTimeMillis();
        recommendationService.getRecommendation("U001", "P001");
        long e = System.currentTimeMillis();
        long d = e - s;
        assertTrue("Time to fulfill request exceeded acceptable time. Time needed was " + d +
                "ms", d <= ACCEPTABLE_TIME_MILLIS);
    }
}
