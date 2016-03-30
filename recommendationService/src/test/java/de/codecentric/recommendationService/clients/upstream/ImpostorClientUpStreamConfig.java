package de.codecentric.recommendationService.clients.upstream;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.codecentric.recommendationService.clients.ImpostorConfig;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by afitz on 23.03.16.
 */
public enum ImpostorClientUpStreamConfig implements ImpostorConfig {

//    tbd! : genrally is to reflect: is the marshalling json<->class meaningful?

    NORMAL("normal"),
    PRESSURE("pressure");


    private String configString;
    private String file;

    private final String config;

    ImpostorClientUpStreamConfig (String i) {

        config = i;

        String dir = "./impostorConfig/UpStream/";

        switch (i) {
            case "normal":
                file = dir + "normal.json";
                break;
            case "pressure":
                file = dir + "pressure.json";
                break;
        }

        try {
            configString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
            System.out.println("Loading config : " + file);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getFileName(){return file;}
    @Override
    public String getJSon() {
        return configString;
    }


    @Override
    public String toString() {
        return config;
    }
}
