package de.codecentric.recommendationService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class LoadTestConfiguration {
    private TestConfiguration config;

    public LoadTestConfiguration() {
        File configYml = new File("./src/test/resources/testConfiguration.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(configYml, TestConfiguration.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Problem reading file " + configYml, e);
        }
    }

    public TestConfiguration get() {
        return config;
    }
}
