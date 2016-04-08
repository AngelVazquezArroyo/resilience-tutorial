package de.codecentric.recommendationService.clients.upstream;

import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ImpostorConfig;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Abstraction of the various configurations for the upstream impostor.
 *
 * @author afitz
 */
public enum ImpostorClientUpstreamConfig implements ImpostorConfig {
    NORMAL("normal"),
    PRESSURE("pressure");

    private static final String DIR = "src/test/resources/upstream/config/";

    private final String file;
    private final String config;
    private String configString;

    ImpostorClientUpstreamConfig(String c) {
        config = c;
        file = DIR + config + ".json";

        try {
            configString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
        }catch (IOException e) {
            throw new ClientException("Reading upstream impostor configuration \"" + config +
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
