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
    DownstreamCompleteParameterChecking("downstream_complete_parameter_checking"),
    DownstreamTimeout("downstream_timeout"),
    DownstreamRetry("downstream_retry"),
    DownstreamCircuitBreaker("downstream_circuit_breaker"),

    UpstreamNormal("upstream_normal"),
    UpstreamCircuitBreaker("upstream_circuit_breaker");

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
