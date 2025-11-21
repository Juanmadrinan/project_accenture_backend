package com.accenture.project_accenture_backend.domain.port.in.product;

import com.accenture.project_accenture_backend.domain.entities.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductStockUseCase {

    Mono<Product> execute(UpdateProductStockCommand command);

    record UpdateProductStockCommand(String productId, int newStock) {
        public UpdateProductStockCommand {
            if (productId == null || productId.isBlank()) {
                throw new IllegalArgumentException("ProductId is null or blank");
            }
            if (newStock < 0) {
                throw new IllegalArgumentException("Update Stock is null or blank");
            }
        }
    }
}
