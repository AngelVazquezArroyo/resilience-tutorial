package de.codecentric.recommendationService.clients.upstream;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.codecentric.recommendationService.clients.ImpostorCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by afitz on 01.04.16.
 */
public enum ImpostorClientUpStreamCommands implements ImpostorCommands{

    PRESSURE_OFF("pressure_off"),
    PRESSURE_ON("pressure_on"),
    PRESSURE_STATUS("pressure_status"),
    SEND_1("send_1"),
    SEND_2("send_2");

    private final Logger logger = LoggerFactory.getLogger(ImpostorClientUpStreamCommands.class);

    private String file;

    private String configString;
    private String config;

    private String dir = "src/test/resources/upStream/commands/";

    ImpostorClientUpStreamCommands (String i) {

        config = i;

        file = dir + i + ".json";

        try {
            configString = new String(java.nio.file.Files.readAllBytes(Paths.get(file)));
            logger.debug("Loading command : " + i);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return this.getJSon();
    }

    @Override
    public String getJSon() {
        return configString;
    }
}
