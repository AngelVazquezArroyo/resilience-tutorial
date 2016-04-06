package de.codecentric.recommendationService.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
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

//        // assume SLF4J is bound to logback in the current environment
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        // print logback's internal status
//        StatusPrinter.print(lc);


        logger.debug("-----------------------------------------------------------");
        logger.debug("load configuration");
        logger.debug("-----------------------------------------------------------");

        try {
            config = mapper.readValue(configYml, TestConfiguration.class);
        } catch (IOException e) {
            logger.error("IOException in LoadTestConfiguratoin: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public TestConfiguration get() {
        return config;
    }

}
