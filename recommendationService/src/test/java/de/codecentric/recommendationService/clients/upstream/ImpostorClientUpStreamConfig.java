package de.codecentric.recommendationService.clients.upstream;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by afitz on 23.03.16.
 */
public enum ImpostorClientUpStreamConfig implements ImpostorConfig {

    NORMAL("normal"),
    PRESSURE("pressure");

    private final Logger logger = LoggerFactory.getLogger(ImpostorClientUpStreamConfig.class);

    private String configString;
    private String file;

    private final String config;

    private String dir = "src/test/resources/upStream/config/";

    ImpostorClientUpStreamConfig(String i) {

        config = i;

        file = dir + i + ".json";

        try {
            configString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
            logger.debug("Loading config : " + file);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getJSon() {
        return configString;
    }


    @Override
    public String toString() {
        return this.getJSon();
    }
}
