package com.accenture.project_accenture_backend.domain.port.in.product;

import com.accenture.project_accenture_backend.domain.entities.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductNameUseCase {

    Mono<Product> execute(UpdateProductNameCommand command);

    record UpdateProductNameCommand(String productId, String newName) {
        public UpdateProductNameCommand {
            if (productId == null || productId.isBlank()) {
                throw new IllegalArgumentException("ProductId is null or blank");
            }
            if (newName == null || newName.isBlank()) {
                throw new IllegalArgumentException("Update name is null or blank");
            }
        }
    }
}