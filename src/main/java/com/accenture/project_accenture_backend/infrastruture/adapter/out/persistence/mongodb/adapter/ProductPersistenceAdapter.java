package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.adapter;

import com.accenture.project_accenture_backend.domain.entities.Product;
import com.accenture.project_accenture_backend.domain.port.out.ProductPersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.ProductId;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.mapper.ProductMapper;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository.BranchReactiveRepository;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository.ProductReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private ProductMapper productMapper;
    private final BranchReactiveRepository branchReactiveRepository;
    private final ProductReactiveRepository productReactiveRepository;

    public ProductPersistenceAdapter(BranchReactiveRepository branchReactiveRepository, ProductReactiveRepository productReactiveRepository, ProductMapper productMapper) {
        this.branchReactiveRepository = branchReactiveRepository;
        this.productReactiveRepository = productReactiveRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.just(product)
                .map(productMapper::toEntity)
                .flatMap(productReactiveRepository::save)
                .map(productMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(ProductId productId) {
        return productReactiveRepository.findById(productId.getValue()).map(productMapper::toDomain);
    }

    @Override
    public Flux<Product> findByFranchiseId(FranchiseId franchiseId) {
        return branchReactiveRepository.findByFranchiseId(franchiseId.getValue())
                .map(branchEntity -> branchEntity.getId())
                .collectList()
                .flatMapMany(branchIds -> {
                    if(branchIds.isEmpty()) {
                        return Flux.empty();
                    }
                    return productReactiveRepository.findByBranchIdIn(branchIds);
                })
                .map(productMapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(BranchId branchId) {
        return productReactiveRepository.findByBranchId(branchId.getValue())
                .map(productMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(ProductId productId) {
        return productReactiveRepository.deleteById(productId.getValue());
    }

    @Override
    public Mono<Boolean> existsById(ProductId productId) {
        return productReactiveRepository.existsById(productId.getValue());
    }
}
