package de.codecentric.recommendationService;

import com.codahale.metrics.*;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;
import de.codecentric.recommendationService.health.AnalysisServiceHealthCheck;
import de.codecentric.recommendationService.resources.RecommendationResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RecommendationService extends Application<RecommendationConfiguration>{

//	tbd! integrate this into the application’s lifecycle?
	private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

	private static final MetricRegistry requestMetrics = new MetricRegistry();

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
	public void run(RecommendationConfiguration recommendationConfiguration,
					Environment recommendationEnvironment) {

		// create & register client(s):
		//*** AnalysisServce ***
		//*** factory will automatically tie our AnalysisServiceClient connection to the lifecycle of our recommendation’s Environment.
		AnalysisServiceClient analysisService = recommendationConfiguration.getAnalysisServiceFactory().build(recommendationEnvironment);
		final RecommendationResource recommendationResource = new RecommendationResource(
				recommendationConfiguration.getDefaultProduct(),
				recommendationConfiguration.getDefaultUser(),
				analysisService
				);

		// register Metrics for service
		Meter requestsMeter = requestMetrics.meter(MetricRegistry.name(RecommendationResource.class, "requests"));
		recommendationEnvironment.metrics().register("requets", requestsMeter);
		requestsMeter.mark();

//		ConsoleReporter reporter = ConsoleReporter.forRegistry(requestMetrics).build();
//		reporter.start(1, TimeUnit.SECONDS); // should expose values every minute


		// register HeatlChecks
//		recommendationEnvironment.healthChecks().register("request", new RequestHealthCheck(requestsMeter));
		recommendationEnvironment.healthChecks().register("AnalyseService", new AnalysisServiceHealthCheck(analysisService));

		//register resources
		recommendationEnvironment.jersey().register(recommendationResource);

	}

}
