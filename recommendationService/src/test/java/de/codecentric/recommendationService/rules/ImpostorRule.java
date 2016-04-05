package de.codecentric.recommendationService.rules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.codecentric.recommendationService.processes.impostorProcess;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by afitz on 05.04.16.
 */
public class ImpostorRule extends ExternalResource {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorRule.class);

    private impostorProcess downStreamProcess = null;
    private impostorProcess upStreamProcess = null;
    private TestConfiguration config;

    public ImpostorRule(TestConfiguration config) {

        downStreamProcess = new impostorProcess();
        downStreamProcess.startImpostorProcess(config.getDownStreamFactory().getHost(), config.getDownStreamFactory().getPort());

            upStreamProcess = new impostorProcess();
            upStreamProcess.startImpostorProcess(config.getUpStreamFactory().getHost(), config.getUpStreamFactory().getPort());
    }

    @Override
    protected void before() throws Throwable {
        super.before();
//        tbd!: test the impostors
    }

    @Override
    protected void after() {

        super.after();
        downStreamProcess.stopImpostorProcess();
        upStreamProcess.stopImpostorProcess();

    }
}
