package de.codecentric.recommendationService.health;

import com.codahale.metrics.health.HealthCheck;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;

/**
 * Created by afitz on 22.03.16.
 */
public class AnalysisServiceHealthCheck extends HealthCheck {

    private AnalysisServiceClient analysisService;

    public AnalysisServiceHealthCheck(AnalysisServiceClient analysisService)
    {
        this.analysisService = analysisService;
    }

    @Override
    protected Result check() throws Exception {

        if (analysisService.ping()) {
            return Result.healthy("Successfull ping AnalyseService: Service available");
        }
        return Result.unhealthy("Can't ping AnalyseService: Service unavailable");

    }
}
