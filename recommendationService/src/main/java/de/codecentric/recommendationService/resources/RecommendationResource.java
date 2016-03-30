package de.codecentric.recommendationService.resources;

import com.codahale.metrics.annotation.Metered;
import com.google.common.base.Optional;
import de.codecentric.recommendationService.api.Recommendation;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceClient;
import de.codecentric.recommendationService.clients.AnalysisClient.AnalysisServiceException;
import de.codecentric.recommendationService.clients.AnalysisClient.Products;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

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
//	@Timed // measures the duration of requests to a resource
	@Metered // measures the rate at which the resource is accessed
//	@ExceptionMetered //measures how often exceptions occur processing the resource
	public Recommendation getRecommendation(@QueryParam("user") Optional<String> user, @QueryParam("product") Optional<String> product) {

		String recommedUser = (user.isPresent() ? user.get() : defaultUser);

//		local access
//		Product recommedProduct = RecommendationLookup.getInstance().getRecommendation(
//				(product.isPresent() ? product.get() : defaultProduct)
//				);

		Products recommendProducts = null;
		try {
			recommendProducts = this.analysisService.executeGetProducts((product.isPresent() ? product.get() : this.defaultProduct));
		} catch (AnalysisServiceException e) {
			logger.error(e.getMessage());
			ArrayList<String> defaultProducts = new ArrayList<String>();
			defaultProducts.add(defaultProduct);
			recommendProducts = new Products(defaultProducts);
		}

		return new Recommendation(recommedUser, recommendProducts.getProducts());
	}

}
