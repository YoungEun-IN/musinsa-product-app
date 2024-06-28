package com.musinsa.product_app.exceptions;

public class DataNotFoundException extends RuntimeException {

	public DataNotFoundException() {
		super("Resource not found");
	}

	public DataNotFoundException(String message) {
		super(message);
	}
}
