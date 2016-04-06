package de.codecentric.recommendationService;

import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

/**
 * Created by afitz on 05.04.16.
 */
public class TestNormal extends RecommendationTestCase {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationTestCase.class);

    @Test //deilver an existing recommendation
    public void testNormal() {

        logger.debug("-----------------------------------------------------------");
        logger.debug(name.getMethodName());
        logger.debug("-----------------------------------------------------------");

        try {

            getImpostorDownStreamClient().setConfig(ImpostorClientDownStreamConfig.NORMAL);

            //return existing product
            Recommendation recommendation = getRecommendationServiceClient().getRecommendation("U001", "P001");
            ArrayList<String> expectedProducts = new ArrayList<String>();
            expectedProducts.add("P002");
            assertArrayEquals("expected produt(s)", expectedProducts.toArray(), recommendation.getProducts().toArray());

            // return dedault product
            recommendation = getRecommendationServiceClient().getRecommendation("U001", "P00T");
            expectedProducts = new ArrayList<String>();
            expectedProducts.add("P001");
            assertArrayEquals("expected produt(s)", expectedProducts.toArray(), recommendation.getProducts().toArray());

        } catch (ServiceClientException e) {
            logger.error(e.getMessage());
            fail();
        }

    }
}
