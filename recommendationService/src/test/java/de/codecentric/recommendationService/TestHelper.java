package de.codecentric.recommendationService;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.codecentric.recommendationService.impostor.Impostor;
import de.codecentric.recommendationService.impostor.ImpostorConfiguration;
import de.codecentric.recommendationService.impostor.ImpostorException;
import de.codecentric.recommendationService.service.Service;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.logging.DefaultLoggingFactory;
import io.dropwizard.server.DefaultServerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

/**
 * A little helper class that makes the test setup a bit more convenient.
 *
 * @author ufr
 */
class TestHelper {
    private static final String HOST = "localhost";
    private static final String DEFAULT_USER = "john.doe";
    private static final String DEFAULT_PRODUCT = "P001";
    private static final String ANALYSIS_SERVICE_PATH = "/get-related";
    private static final long HTTP_CLIENT_TIMEOUT_MS = 2000L;
    private static final Level LOG_LEVEL = Level.ERROR;

    private static final String IMPOSTOR_PROCESS_CONFIGURATION_PATH =
            "./src/test/resources/impostor/impostor.yml";
    private static final long MAX_WAIT_TIME_FOR_IMPOSTOR_MS = 1000L;
    private static final long WAIT_BEFORE_RETRY_MS = 50L;

    public static Impostor createImpostor(int port, ImpostorConfiguration configuration) throws
            IOException {
        Process process;

        ImpostorProcessConfiguration pc = loadImpostorProcessConfiguration();
        String[] command = {pc.getExecutable(), HOST + ":" + port};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(null);
        process = builder.start();
        Impostor impostor = new Impostor(HOST, port, process);
        waitForImpostorToBecomeReady(impostor);
        impostor.setConfig(configuration);
        return impostor;
    }

    public static RecommendationConfiguration createServiceConfiguration(int port, int adminPort,
                                                                         int analysisServicePort) {
        return createServiceConfiguration(port, adminPort, analysisServicePort, 0);
    }

    public static RecommendationConfiguration
    createServiceConfiguration(int port, int adminPort,
                               int analysisServicePort,
                               int analysisServiceFailoverPort) {
        RecommendationConfiguration c = new RecommendationConfiguration();
        c.setDefaultUser(DEFAULT_USER);
        c.setDefaultProduct(DEFAULT_PRODUCT);
        c.getAnalysisService().setHost(HOST);
        c.getAnalysisService().setPort(analysisServicePort);
        c.getAnalysisService().setPortFailover(analysisServiceFailoverPort);
        c.getAnalysisService().setPath(ANALYSIS_SERVICE_PATH);
        c.getAnalysisService().setTimeout(HTTP_CLIENT_TIMEOUT_MS);
        DefaultServerFactory s = (DefaultServerFactory) c.getServerFactory();
        HttpConnectorFactory a = (HttpConnectorFactory) s.getApplicationConnectors().get(0);
        a.setPort(port);
        a = (HttpConnectorFactory) s.getAdminConnectors().get(0);
        a.setPort(adminPort);
        DefaultLoggingFactory l = (DefaultLoggingFactory) c.getLoggingFactory();
        l.setLevel(LOG_LEVEL);
        return c;
    }

    public static Service createService(int port, int adminPort) {
        return new Service(HOST, port, adminPort);
    }

    private static ImpostorProcessConfiguration loadImpostorProcessConfiguration() throws
            IOException {
        File f = new File(IMPOSTOR_PROCESS_CONFIGURATION_PATH);
        ObjectMapper m = new ObjectMapper(new YAMLFactory());
        return m.readValue(f, ImpostorProcessConfiguration.class);
    }

    private static void waitForImpostorToBecomeReady(Impostor impostor) {
        boolean up = false;
        long s = System.currentTimeMillis();

        while (!up && ((System.currentTimeMillis() - s) < MAX_WAIT_TIME_FOR_IMPOSTOR_MS)) {
            pause(WAIT_BEFORE_RETRY_MS);
            try {
                up = impostor.isHealthy();
            } catch (ImpostorException e) {
                if (!(e.getCause() instanceof ConnectException)) {
                    throw e;
                }
            }
        }
    }

    private static void pause(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}

class ImpostorProcessConfiguration {
    private String executable = "";

    @JsonProperty
    public String getExecutable() {
        return executable;
    }

    @JsonProperty
    public void setExecutable(String executable) {
        this.executable = executable;
    }
}
