package de.codecentric.recommendationService.processes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by afitz on 05.04.16.
 */
public class ImpostorProcess {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorProcess.class);

    private Process process;
    private ProcessBuilder proBuilder;

    public void startImpostorProcess(String host, int port) {

        String dir = "./../impostor/";

        String[] command = {dir + "impostor", host +":" + port};
        proBuilder = new ProcessBuilder(command);

        // set working directory to bin/impostor
        proBuilder.directory(new File(dir));

        try {
            process = proBuilder.start();
            logger.debug("start Process: " + process.toString() + " with " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopImpostorProcess(){
        process.destroy();
    }

    public String getProcessId(){
        return process.toString();
    }
}
