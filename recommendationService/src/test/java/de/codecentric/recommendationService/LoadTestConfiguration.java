package de.codecentric.recommendationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ServiceClient;
import de.codecentric.recommendationService.clients.ServiceClientException;
import de.codecentric.recommendationService.clients.service.ServiceClientRecommendationFactory;
import de.codecentric.recommendationService.rules.TestConfiguration;
import de.codecentric.recommendationService.rules.ImpostorRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by afitz on 05.04.16.
 */
public class LoadTestConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(LoadTestConfiguration.class);

    private TestConfiguration config;

    public LoadTestConfiguration() {
        logger.info("-----------------------------------------------------------");
        logger.info("load configuration");
        logger.info("-----------------------------------------------------------");

        File configYml = new File("./src/test/resources/testConfiguration.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(configYml, TestConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
     public TestConfiguration build(){
         return config;
     }

}
