package de.codecentric.recommendationService;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;
import de.codecentric.recommendationService.health.AnalysisServiceHealthCheck;
import de.codecentric.recommendationService.resources.RecommendationResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The core recommendation service class (or the application class in terms of DropWizard). All
 * basic setup, configuration and wiring is done in this class.
 *
 * @author afitz
 */
public class RecommendationService extends Application<RecommendationConfiguration> {
    public static void main(String[] args) throws Exception {
        new RecommendationService().run(args);
    }

    @Override
    public String getName() {
        return "Awesome Recommendation Service";
    }

    @Override
    public void initialize(Bootstrap<RecommendationConfiguration> bootstrap) {
        // nothing to do at the moment
    }

    @Override
    public void run(RecommendationConfiguration conf, Environment env) {
        final AnalysisServiceClient analysisService = conf.getAnalysisService().build(env);
        final RecommendationResource recommendationResource = new RecommendationResource(
                conf.getDefaultProduct(), conf.getDefaultUser(), analysisService);

        Meter requestMeter = env.metrics().meter(MetricRegistry.name(RecommendationResource.class,
                "requests"));
        env.metrics().register("requests", requestMeter);

        env.healthChecks().register("AnalysisService", new AnalysisServiceHealthCheck
                (analysisService));
        env.jersey().register(recommendationResource);
    }
}
