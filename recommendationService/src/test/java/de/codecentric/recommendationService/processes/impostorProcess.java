package de.codecentric.recommendationService.processes;

import java.io.File;
import java.io.IOException;

/**
 * Created by afitz on 05.04.16.
 */
public class impostorProcess {

    private Process process;
    private ProcessBuilder probuilder;

    public void startImpostorProcess(String host, int port) {

        String dir = "./../impostor/";

//        String[] command = {"/Users/afitz/Dropbox/resilience-tutorial/impostor/impostor", "localhost:8102"};
        String[] command = {dir + "impostor", host +":" + port};
        probuilder = new ProcessBuilder(command);
        //You can set up your work directory
        probuilder.directory(new File(dir));

        try {
            process = probuilder.start();
            System.out.println("start Process: " + process.toString() + " with " + host + ":" + port);
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
