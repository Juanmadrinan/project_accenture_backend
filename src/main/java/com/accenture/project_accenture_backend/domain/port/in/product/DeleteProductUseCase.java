package com.accenture.project_accenture_backend.domain.port.in.product;

import reactor.core.publisher.Mono;

public interface DeleteProductUseCase {

    Mono<Void> execute(DeleteProductCommand command);

    record DeleteProductCommand(String productId) {
        public DeleteProductCommand {
            if (productId == null || productId.isBlank()) {
                throw new IllegalArgumentException("ProductId is null or blank");
            }
        }
    }
}