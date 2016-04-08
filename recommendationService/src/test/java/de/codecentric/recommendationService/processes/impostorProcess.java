package de.codecentric.recommendationService.processes;

import java.io.IOException;

/**
 * Abstracts an external impostor process.
 *
 * @author afitz
 */
public class ImpostorProcess {
    private Process process;
    private ProcessBuilder builder;

    public void startImpostorProcess(String path, String host, int port) throws IOException {

        String[] command = {path, host + ":" + port};
        builder = new ProcessBuilder(command);
        builder.directory(null);
        process = builder.start();
    }

    public void stopImpostorProcess() {
        process.destroy();
    }
}
