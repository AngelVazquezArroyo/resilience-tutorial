package de.codecentric.recommendationService.health;

import com.codahale.metrics.health.HealthCheck;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Health check for the product analysis service.
 *
 * @author afitz
 */
public class AnalysisServiceHealthCheck extends HealthCheck {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceHealthCheck.class);
    private AnalysisServiceClient analysisService;

    public AnalysisServiceHealthCheck(AnalysisServiceClient analysisService) {
        this.analysisService = analysisService;
    }

    @Override
    protected Result check() throws Exception {
        if (analysisService.ping()) {
            logger.debug("Health check status for analysis service is: Healthy");
            return Result.healthy("Analysis service accessible");
        }
        logger.debug("Health check status for analysis service is: Unhealthy");
        return Result.unhealthy("Analysis service not accessible");
    }
}
