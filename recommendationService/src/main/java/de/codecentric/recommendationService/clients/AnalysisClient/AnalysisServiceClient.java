package de.codecentric.recommendationService.clients.AnalysisClient;

import de.codecentric.recommendationService.core.Products;

/**
 * The interface each client that wraps the access to the product analysis service must implement.
 *
 * Created by afitz on 21.03.16.
 */
public interface AnalysisServiceClient {
    /**
     * Return products that are a cross- or up-sale with respect to the given product.
     *
     * @param product a given product
     * @return a list of cross- or up-selling products
     * @throws AnalysisServiceException if the access of the underlying product analysis service
     * encounters an unexpected problem
     */
    public Products getCrossUpSellingProducts(String product) throws AnalysisServiceException;

    /**
     * Checks if the underlying product analysis service is accessible using a synthetic
     * transaction.
     *
     * @return {@code true} if the service is accessible, {@code false} otherwise
     */
    public boolean ping();
}
