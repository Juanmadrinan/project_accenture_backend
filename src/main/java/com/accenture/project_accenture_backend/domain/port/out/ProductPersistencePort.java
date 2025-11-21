package com.accenture.project_accenture_backend.domain.port.out;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.entities.Product;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.ProductId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {
    Mono<Product> save(Product product);
    Mono<Product> findById(ProductId productId);
    Flux<Product> findByFranchiseId(FranchiseId franchiseId);
    Flux<Product> findByBranchId(BranchId branchId);
    Mono<Void> deleteById(ProductId productId);
    Mono<Boolean> existsById(ProductId productId);
}

