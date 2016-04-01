package de.codecentric.recommendationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.codecentric.recommendationService.RecommendationConfiguration;
import de.codecentric.recommendationService.RecommendationService;
import de.codecentric.recommendationService.TestBulkheads;
import de.codecentric.recommendationService.TestConfiguration;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.clients.upstream.ImpostorClientUpStreamConfig;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by afitz on 31.03.16.
 */
public class TestCompleteParamterChecking {

    private static final Logger logger = LoggerFactory.getLogger(TestCompleteParamterChecking.class);

    @Test
    public void testNormal() throws IOException {

            logger.info("execute TestCompleteParamterChecking");

    }

}




