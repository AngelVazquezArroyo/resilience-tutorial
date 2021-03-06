package de.codecentric.recommendationService.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Products {
        @JsonProperty("products")
        private List<String> products;

        @JsonProperty
        public List<String> getProducts() {
            return products;
        }

        @JsonProperty
        public void setProducts(List<String> products) {
            this.products = products;
        }

        public Products() {
            // Jackson deserialization
        }

        public Products(List<String> products){
            this.products = products;
        }
}
