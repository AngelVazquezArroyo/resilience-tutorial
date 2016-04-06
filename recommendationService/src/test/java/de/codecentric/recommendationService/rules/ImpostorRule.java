package de.codecentric.recommendationService.rules;

import de.codecentric.recommendationService.config.TestConfiguration;
import de.codecentric.recommendationService.processes.ImpostorProcess;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by afitz on 05.04.16.
 */
public class ImpostorRule extends ExternalResource {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorRule.class);

    private ImpostorProcess downStreamProcess = null;
    private ImpostorProcess upStreamProcess = null;
    private TestConfiguration config;

    public ImpostorRule(TestConfiguration config) {

        downStreamProcess = new ImpostorProcess();
        upStreamProcess = new ImpostorProcess();
        this.config = config;
    }

    @Override
    protected void before() throws Throwable {
        downStreamProcess.startImpostorProcess(config.getDownStreamFactory().getHost(), config.getDownStreamFactory().getPort());
        upStreamProcess.startImpostorProcess(config.getUpStreamFactory().getHost(), config.getUpStreamFactory().getPort());
    }

    @Override
    protected void after() {

        super.after();
        downStreamProcess.stopImpostorProcess();
        upStreamProcess.stopImpostorProcess();

    }
}
