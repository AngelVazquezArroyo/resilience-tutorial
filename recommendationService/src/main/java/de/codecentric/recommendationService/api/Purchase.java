package de.codecentric.recommendationService.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Purchase {

	private String user;
	private String product;

	@JsonProperty
	public String getUser() {
		return user;
	}

	@JsonProperty
	public void setUser(String user) {
		this.user = user;
	}	
	
	@JsonProperty
	public String getProduct() {
		return product;
	}

	@JsonProperty
	public void setProduct(String product) {
		this.product = product;
	}

	public Purchase() {
		 // Jackson deserialization
	}

	public Purchase(String user, String product){
		 this.user = user;
		 this.product = product;
	}

}
