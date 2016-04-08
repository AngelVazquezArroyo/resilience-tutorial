package de.codecentric.recommendationService;

import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownstreamConfig;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

/**
 * Test suite with "good cases" that should succeed with the initial recommendation service
 * implementation.
 *
 * @author afitz
 */
public class TestNormal extends RecommendationTestCase {

    @Test
    public void shouldDeliverAnExistingRecommendation() {
        try {
            getImpostorDownstreamClient().setConfig(ImpostorClientDownstreamConfig.NORMAL);
            Recommendation recommendation = getRecommendationServiceClient().getRecommendation("U001", "P001");
            ArrayList<String> expectedProducts = new ArrayList<>();
            expectedProducts.add("P002");
            assertArrayEquals("expected product(s)", expectedProducts.toArray(), recommendation.getProducts().toArray());
        } catch (ClientException e) {
            fail(e.getMessage());
        }
    }
}
