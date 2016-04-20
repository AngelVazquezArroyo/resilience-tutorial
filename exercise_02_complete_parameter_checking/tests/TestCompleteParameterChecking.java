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
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Test suite with test cases for the "complete parameter checking" section of the resilience
 * tutorial.
 *
 * @author ufr
 */
public class TestCompleteParameterChecking {
    private static final int BASE_PORT = 8510;

    private static final int ANALYSIS_SERVICE_PORT = BASE_PORT;
    private static final int RECOMMENDATION_SERVICE_PORT = BASE_PORT + 1;
    private static final int RECOMMENDATION_SERVICE_ADMIN_PORT = BASE_PORT + 2;

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
                ImpostorConfiguration.DownstreamCompleteParameterChecking);
        recommendationService = TestHelper.createService(RECOMMENDATION_SERVICE_PORT,
                RECOMMENDATION_SERVICE_ADMIN_PORT);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        analysisService.destroy();
    }

    @Test
    public void shouldRejectEmptyParameters() {
        try {
            Recommendation recommendation = recommendationService.getRecommendation(null, null);
            fail("This call should result in an exception");
        } catch (ServiceException e) {
            assertTrue("Status from service call should be 400", e.getMessage().contains("400"));
        }
    }

    @Test
    public void shouldRejectMissingProductParameter() {
        try {
            Recommendation recommendation = recommendationService.getRecommendation("user=U001");
            fail("This call should result in an exception");
        } catch (ServiceException e) {
            assertTrue("Status from service call should be 400", e.getMessage().contains("400"));
        }
    }

    @Test
    public void shouldRejectInvalidProduct() {
        // Product format should be Pnnn (n ist a number between 0 and 9)
        try {
            Recommendation recommendation = recommendationService.getRecommendation("U001",
                    "dummy");
            fail("This call should result in an exception");
        } catch (ServiceException e) {
            assertTrue("Status from service call should be 400", e.getMessage().contains("400"));
        }
    }

    @Test
    public void shouldHandleBrokenResponseGracefully() {
        try {
            Recommendation recommendation = recommendationService.getRecommendation("U001",
                    "P999");
            assertTrue("A broken response should result in an empty recommendation",
                    recommendation.getProducts().size() == 0);
        } catch (ServiceException e) {
            fail("This call should not result in a service exception with message: " + e
                    .getMessage());
        }
    }
}
