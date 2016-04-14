package de.codecentric.recommendationService.impostor;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Abstraction of the various configurations for the impostor.
 *
 * @author afitz
 * @author ufr
 */
public enum ImpostorCommand {
    PressureOff("pressure_off"),
    PressureOn("pressure_on"),
    PressureStatus("pressure_status");

    private static final String DIR = "src/test/resources/impostor/command/";

    private final String basename;
    private String command;

    ImpostorCommand(String b) {
        basename = b;
        String file = DIR + basename + ".json";

        try {
            command = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
        }catch (IOException e) {
            throw new ImpostorException("Reading impostor command \"" + basename +
                    "\" from file failed (" + e.getClass().getName() + ", " +
                    e.getMessage() + ")", e);
        }
    }

    public String get() {
        return command;
    }

    @Override
    public String toString() {
        return basename;
    }
}
