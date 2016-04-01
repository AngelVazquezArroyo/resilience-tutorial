package de.codecentric.recommendationService.clients.downstream;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by afitz on 24.03.16.
 */
public enum ImpostorClientDownStreamConfig implements ImpostorConfig {
    //    tbd! : genrally is to reflect: is the marshalling json<->class meaningful?

    NORMAL("normal"),
    ONETIMELATENCY("one_time_latency"),
    RECURRINGLATENCY("recurring_latency");

    private final Logger logger = LoggerFactory.getLogger(ImpostorClientDownStreamConfig.class);

    private String configString;
    private String file;

    private final String config;

    ImpostorClientDownStreamConfig(String i) {

        config = i;

        String dir = "src/test/resources/impostorConfig/DownStream/";

        switch (i) {
            case "normal":
                file = dir + "normal.json";
                break;
            case "one_time_latency":
                file = dir + "one_time_latency.json";
                break;
            case "recurring_latency":
                file = dir + "recurring_latency.json";
                break;

        }

        try {
            configString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
            configString = configString.concat(new String(java.nio.file.Files.readAllBytes(Paths.get(dir + "responses.json"))));
            configString = configString.concat("}");

            logger.debug("JSON: " + configString);

            logger.info("Loading config : " + file);

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
