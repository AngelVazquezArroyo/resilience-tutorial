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
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

/**
 * Test suite with "good cases" that should succeed with the initial recommendation service
 * implementation.
 *
 * @author afitz
 * @author ufr
 */
public class TestNormal {
    private static final int BASE_PORT = 8500;

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
                ImpostorConfiguration.DownstreamNormal);
        recommendationService = TestHelper.createService(RECOMMENDATION_SERVICE_PORT,
                RECOMMENDATION_SERVICE_ADMIN_PORT);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        analysisService.destroy();
    }

    @Test
    public void shouldDeliverAnExistingRecommendation() {
        try {
            Recommendation recommendation = recommendationService.getRecommendation("U001",
                    "P001");
            ArrayList<String> expectedProducts = new ArrayList<>();
            expectedProducts.add("P002");
            assertArrayEquals("expected product(s)", expectedProducts.toArray(), recommendation.getProducts().toArray());
        } catch (ServiceException e) {
            fail(e.getMessage());
        }
    }
}
