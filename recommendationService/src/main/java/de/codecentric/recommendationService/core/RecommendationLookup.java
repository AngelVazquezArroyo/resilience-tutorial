package de.codecentric.recommendationService.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by afitz on 15.03.16.
 */
public class RecommendationLookup {
    private static RecommendationLookup ourInstance = new RecommendationLookup();

    private String standardProductRecommendation = "P999";
    // Map<User, Product>
    private final Map<String, String> recommendationMap = new HashMap<String, String>();


    public static RecommendationLookup getInstance() {
        return ourInstance;
    }

    private RecommendationLookup() {
        recommendationMap.put("P00T", "P001");
        recommendationMap.put("P001", "P002");
        recommendationMap.put("P002", "P003");
        recommendationMap.put("P003", "P003");

    }

    public Product getRecommendation (Product product) {
        return new Product((recommendationMap.containsKey(product.getId()) ? recommendationMap.get(product.getId()) : standardProductRecommendation));
    }


}
