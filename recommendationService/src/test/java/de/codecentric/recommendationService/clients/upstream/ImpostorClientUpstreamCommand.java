package de.codecentric.recommendationService.clients.upstream;

import de.codecentric.recommendationService.clients.ClientException;
import de.codecentric.recommendationService.clients.ImpostorCommands;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Abstraction of the various commands for the upstream impostor.
 *
 * @author afitz
 */
public enum ImpostorClientUpstreamCommand implements ImpostorCommands{
    PRESSURE_OFF("pressure_off"),
    PRESSURE_ON("pressure_on"),
    PRESSURE_STATUS("pressure_status"),
    SEND_1("send_1"),
    SEND_2("send_2");

    private static final String DIR = "src/test/resources/upstream/commands/";

    private final String file;
    private final String command;
    private String commandString;

    ImpostorClientUpstreamCommand(String c) {
        command = c;
        file = DIR + command + ".json";

        try {
            commandString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
        }catch (IOException e) {
            throw new ClientException("Reading upstream impostor command \"" + command +
                    "\" from file failed (see embedded exception)", e);
        }
    }

    @Override
    public String getJSon() {
        return commandString;
    }

    @Override
    public String toString() {
        return this.getJSon();
    }
}
