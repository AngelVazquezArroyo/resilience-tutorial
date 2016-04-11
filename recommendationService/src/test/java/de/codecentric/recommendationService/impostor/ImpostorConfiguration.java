package de.codecentric.recommendationService.impostor;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Abstraction of the various configurations for the impostor.
 *
 * @author afitz
 * @author ufr
 */
public enum ImpostorConfiguration {
    DownstreamNormal("downstream_normal"),
    DownstreamOneTimeLatency("downstream_one_time_latency"),
    DownstreamRecurringLatency("downstream_recurring_latency"),

    UpstreamNormal("upstream_normal"),
    UpstreamPressure("upstream_pressure");

    private static final String DIR = "src/test/resources/impostor/config/";

    private final String basename;
    private String configuration;

    ImpostorConfiguration(String b) {
        basename = b;
        String file = DIR + basename + ".json";

        try {
            configuration = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
        }catch (IOException e) {
            throw new ImpostorException("Reading impostor configuration \"" + basename +
                    "\" from file failed (" + e.getClass().getName() + ", " +
                    "" + e.getMessage() + ")", e);
        }
    }

    public String get() {
        return configuration;
    }

    @Override
    public String toString() {
        return basename;
    }
}
