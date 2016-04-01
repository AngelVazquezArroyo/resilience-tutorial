package de.codecentric.recommendationService.health;

import com.codahale.metrics.health.HealthCheck;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by afitz on 22.03.16.
 */
public class AnalysisServiceHealthCheck extends HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceHealthCheck.class);

    private AnalysisServiceClient analysisService;

    public AnalysisServiceHealthCheck(AnalysisServiceClient analysisService)
    {
        this.analysisService = analysisService;
    }

    @Override
    protected Result check() throws Exception {

        if (analysisService.ping()) {
            logger.info("Successfull ping AnalyseService: Service available");
            return Result.healthy("Successfull ping AnalyseService: Service available");
        }
        logger.error("Can't ping AnalyseService: Service unavailable");
        return Result.unhealthy("Can't ping AnalyseService: Service unavailable");
    }
}
