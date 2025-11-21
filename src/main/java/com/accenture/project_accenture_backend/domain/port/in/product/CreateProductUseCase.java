package com.accenture.project_accenture_backend.domain.port.in.product;

import com.accenture.project_accenture_backend.domain.entities.Product;
import reactor.core.publisher.Mono;

public interface CreateProductUseCase {

    Mono<Product> execute(CreateProductCommand command);

    record CreateProductCommand(String branchId, String name, int initialStock) {
        public CreateProductCommand {
            if (branchId == null || branchId.isBlank()) {
                throw new IllegalArgumentException("BranchId is null or blank");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name is null or blank");
            }
            if (initialStock < 0) {
                throw new IllegalArgumentException("Stock is negative");
            }
        }
    }
}