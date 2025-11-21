package com.accenture.project_accenture_backend.domain.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String productId) {
        super(String.format("Product with id not found", productId));
    }

}
