package de.codecentric.testrunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.codecentric.testrunner.clients.ImpostorClient;
import de.codecentric.testrunner.clients.ServiceClient;
import de.codecentric.testrunner.clients.ServiceClientException;
import de.codecentric.testrunner.clients.downstream.ImpostorClientDownStreamConfig;
import de.codecentric.testrunner.clients.downstream.ImpostorDownStreamConfig;
import de.codecentric.testrunner.clients.service.ServiceHealthResult;
import de.codecentric.testrunner.clients.service.ServiceHealthStatusStructure;
import de.codecentric.testrunner.clients.service.ServiceHealthStatusCode;
import de.codecentric.testrunner.clients.upstream.ImpostorClientUpStreamConfig;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by afitz on 23.03.16.
 */
public class SampleTest {

    private ImpostorClient impostorUpStream = null;
    private ImpostorClient impostorDownStream = null;
    private ServiceClient recommendationClient = null;

    @Before
    public void initializeImpostor() throws IOException {

        // load TestConfiguration
        File yml = new File("./testrunner.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestConfiguration config = mapper.readValue(yml, TestConfiguration.class);

        // build clients to all impostors and clients
        impostorUpStream = config.getUpStreamFactory().build(); //with config.normal
        impostorDownStream = config.getDownStreamFactory().build(); //with config.normal
        recommendationClient = config.getAwesomeRecommendationFactory().build();
    }


    @Test
    public void testNormal() throws IOException {

        // configure impostorDownStream
        impostorDownStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);

        try {
            assertEquals("Status code must be 200", 200, recommendationClient.getHealthy().getStatusCode());
        } catch (ServiceClientException e) {
            e.printStackTrace();
        }
//        System.out.println("Message :" + result.getMessage());
//        System.out.println("Status  :" + result.getStatusCode());
//        System.out.println("Entity  :" + result.getEntity());


    }

    @Test
    public void testRecurringLatency() throws IOException {

        // configure impostorDownStream
        impostorDownStream.setConfig(ImpostorClientDownStreamConfig.RECURRINGLATENCY);
        try {
            assertEquals("Status code must be 200", 200, recommendationClient.getHealthy().getStatusCode());
        } catch (ServiceClientException e) {
            e.printStackTrace();
        }
//        System.out.println("Message :" + result.getMessage());
//        System.out.println("Status  :" + result.getStatusCode());
//        System.out.println("Entity  :" + result.getEntity());
    }

    @After
    public void resetImpostor() {
        // set for all Impostor' the config to NORMAL
        impostorDownStream.setConfig(ImpostorClientDownStreamConfig.NORMAL);
        impostorUpStream.setConfig(ImpostorClientUpStreamConfig.NORMAL);
    }

}
