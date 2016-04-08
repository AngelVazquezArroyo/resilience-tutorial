package de.codecentric.recommendationService.rules;

import de.codecentric.recommendationService.config.TestConfiguration;
import de.codecentric.recommendationService.processes.ImpostorProcess;
import org.junit.rules.ExternalResource;

/**
 * JUnit test rule that provides an upstream and downstream impostor.
 *
 * @author afitz
 */
public class ImpostorRule extends ExternalResource {
    private ImpostorProcess downstreamProcess = null;
    private ImpostorProcess upstreamProcess = null;
    private TestConfiguration config;

    public ImpostorRule(TestConfiguration config) {
        downstreamProcess = new ImpostorProcess();
        upstreamProcess = new ImpostorProcess();
        this.config = config;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        downstreamProcess.startImpostorProcess(config.getDownstreamFactory().getPath(),
                config.getDownstreamFactory().getHost(),
                config.getDownstreamFactory().getPort());
        upstreamProcess.startImpostorProcess(config.getDownstreamFactory().getPath(),
                config.getUpstreamFactory().getHost(),
                config.getUpstreamFactory().getPort());
    }

    @Override
    protected void after() {
        downstreamProcess.stopImpostorProcess();
        upstreamProcess.stopImpostorProcess();
        super.after();
    }
}
