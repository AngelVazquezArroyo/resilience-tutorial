package de.codecentric.recommendationService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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

        File configYml = new File("./src/test/resources/testConfiguration.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(configYml, TestConfiguration.class);
        } catch (IOException e) {
            logger.error("IOException in LoadTestConfiguration: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public TestConfiguration get() {
        return config;
    }

}
