package de.codecentric.recommendationService;

import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.impostor.Impostor;
import de.codecentric.recommendationService.impostor.ImpostorConfiguration;
import de.codecentric.recommendationService.service.Service;
import de.codecentric.recommendationService.service.ServiceException;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test suite with test cases for the "circuit breaker" section of the resilience tutorial.
 *
 * @author ufr
 */
public class TestCircuitBreaker {
    private static final int BASE_PORT = 8540;

    private static final int ANALYSIS_SERVICE_PORT = BASE_PORT;
    private static final int RECOMMENDATION_SERVICE_PORT = BASE_PORT + 1;
    private static final int RECOMMENDATION_SERVICE_ADMIN_PORT = BASE_PORT + 2;

    private static final int REQUESTS = 20;
    private static final long DELAY = 100L;

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
                ImpostorConfiguration.DownstreamCircuitBreaker);
        recommendationService = TestHelper.createService(RECOMMENDATION_SERVICE_PORT,
                RECOMMENDATION_SERVICE_ADMIN_PORT);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        analysisService.destroy();
    }

    @Ignore
    @Test
    public void shouldTripCircuitBreaker() {
        List<Recommendation> lr = new ArrayList<>(REQUESTS);
        List<ServiceException> le = new ArrayList<>(REQUESTS);
        for (int i = 0; i < REQUESTS; i++) {
            try {
                Recommendation r = recommendationService.getRecommendation("U001", "P002");
                lr.add(r);
            } catch (ServiceException e) {
                le.add(e);
            }
            pause(DELAY);
        }
        assertEquals("All requests should have been processed", REQUESTS, lr.size() + le.size());
        int c = 0;
        for (ServiceException e : le) {
            if (e.getMessage().contains("504")) {
                c++;
            }
        }
        assertTrue("The circuit breaker should have tripped", c > 0);
        assertEquals("Nothing strange should have happened", le.size(), c);
    }

    private static void pause(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}
