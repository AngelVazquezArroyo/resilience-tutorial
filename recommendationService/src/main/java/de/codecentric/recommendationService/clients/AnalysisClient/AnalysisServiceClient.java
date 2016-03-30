package de.codecentric.recommendationService.clients.AnalysisClient;

/**
 * Created by afitz on 21.03.16.
 */
public interface AnalysisServiceClient {
    public Products executeGetProducts(String product) throws AnalysisServiceException;
    public Boolean ping();
}
