package de.codecentric.recommendationService;

import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.impostor.Impostor;
import de.codecentric.recommendationService.impostor.ImpostorConfiguration;
import de.codecentric.recommendationService.service.Service;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test suite with test cases for the "retry" section of the resilience tutorial.
 *
 * @author ufr
 */
public class TestFailover {
    private static final int BASE_PORT = 8550;

    private static final int ANALYSIS_SERVICE_PORT = BASE_PORT;
    private static final int ANALYSIS_SERVICE_FAILOVER_PORT = BASE_PORT + 1;
    private static final int RECOMMENDATION_SERVICE_PORT = BASE_PORT + 2;
    private static final int RECOMMENDATION_SERVICE_ADMIN_PORT = BASE_PORT + 3;

    private static final long ACCEPTABLE_TIME_MILLIS = 300L;

    private static Impostor analysisService;
    private static Impostor analysisServiceFailover;
    private static Service recommendationService;

    @ClassRule
    public static final DropwizardAppRule<RecommendationConfiguration> SERVICE_RULE =
            new DropwizardAppRule<>(RecommendationService.class,
                    TestHelper.createServiceConfiguration(RECOMMENDATION_SERVICE_PORT,
                            RECOMMENDATION_SERVICE_ADMIN_PORT, ANALYSIS_SERVICE_PORT,
                            ANALYSIS_SERVICE_FAILOVER_PORT));

    @BeforeClass
    public static void initialize() throws IOException {
        analysisService = TestHelper.createImpostor(ANALYSIS_SERVICE_PORT,
                ImpostorConfiguration.DownstreamFailoverSlow);
        analysisServiceFailover = TestHelper.createImpostor(ANALYSIS_SERVICE_FAILOVER_PORT,
                ImpostorConfiguration.DownstreamFailoverFast);
        recommendationService = TestHelper.createService(RECOMMENDATION_SERVICE_PORT,
                RECOMMENDATION_SERVICE_ADMIN_PORT);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        analysisService.destroy();
        analysisServiceFailover.destroy();
    }

    @Test
    public void shouldGetResponseFromFailoverService() {
        Recommendation r = recommendationService.getRecommendation("U001", "P001");
        assertTrue("Result should not be empty", r.getProducts().size() > 0);
        assertEquals("Result should contain product \"P003\". Found \"" + r.getProducts().get(0) +
                "\" instead", "P003", r.getProducts().get(0));
    }
}
