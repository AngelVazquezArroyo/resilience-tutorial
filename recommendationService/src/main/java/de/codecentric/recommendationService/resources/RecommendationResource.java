package de.codecentric.recommendationService.resources;

import com.codahale.metrics.annotation.Metered;
import com.google.common.base.Optional;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceException;
import de.codecentric.recommendationService.core.Products;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Implementation of the recommendation service HTTP API.
 *
 * @author afitz
 */
@Path("/recommendation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationResource {
	private static final Logger logger = LoggerFactory.getLogger(RecommendationResource.class);

	private final String defaultProduct;
	private final String defaultUser;
	private final AnalysisServiceClient analysisService;

	public RecommendationResource(String defaultProduct, String defaultUser, AnalysisServiceClient analysisService) {
		this.defaultProduct = defaultProduct;
		this.defaultUser = defaultUser;
		this.analysisService = analysisService;
	}

	@GET
	@Metered(name = "getRecommendation")
	public Recommendation getRecommendation(@QueryParam("user") Optional<String> user, @QueryParam("product") Optional<String> product) {
		Products products;
		try {
			products = analysisService.getCrossUpSellingProducts(product.or
                    (defaultProduct));
		} catch (AnalysisServiceException e) {
			logger.info("Accessing analysis service failed.", e);
			products = defaultProducts();
		}
		return new Recommendation(user.or(defaultUser), products.getProducts());
	}

    private Products defaultProducts() {
        ArrayList<String> defaultProducts = new ArrayList<>();
        defaultProducts.add(defaultProduct);
        return new Products(defaultProducts);
    }
}
