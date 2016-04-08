package de.codecentric.recommendationService.clients.downstream;

import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ImpostorConfig;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Abstraction of the various configurations for the downstream impostor.
 *
 * @author afitz
 */
public enum ImpostorClientDownstreamConfig implements ImpostorConfig {
    NORMAL("normal"),
    ONE_TIME_LATENCY("one_time_latency"),
    RECURRING_LATENCY("recurring_latency");

    private static final String DIR = "src/test/resources/downstream/config/";

    private final String file;
    private final String config;
    private String configString;

    ImpostorClientDownstreamConfig(String c) {
        config = c;
        file = DIR + config + ".json";

        try {
            configString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
            configString = configString.concat(new String(java.nio.file.Files.readAllBytes(Paths
                    .get(DIR + "responses.json"))));
            configString = configString.concat("}");
        }catch (IOException e) {
            throw new ClientException("Reading downstream impostor configuration \"" + config +
                    "\" from file failed (see embedded exception)", e);
        }
    }

    @Override
    public String getJSon() {
        return configString;
    }

    @Override
    public String toString() {
        return config;
    }
}
