package de.codecentric.recommendationService.core;

/**
 * Created by afitz on 15.03.16.
 */

public class Product {
	
	private String id = null;

	public Product (String id){
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
