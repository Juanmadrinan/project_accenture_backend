package com.accenture.project_accenture_backend.application.service;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.entities.Product;
import com.accenture.project_accenture_backend.domain.exception.BranchNotFoundException;
import com.accenture.project_accenture_backend.domain.exception.ProductNotFoundException;
import com.accenture.project_accenture_backend.domain.port.in.product.*;
import com.accenture.project_accenture_backend.domain.port.out.BranchPersistencePort;
import com.accenture.project_accenture_backend.domain.port.out.ProductPersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.Stock;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.ProductId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService implements
        CreateProductUseCase,
        UpdateProductNameUseCase,
        UpdateProductStockUseCase,
        DeleteProductUseCase,
        GetTopStockProductsByBranchUseCase {

    private final ProductPersistencePort productPersistencePort;
    private final BranchPersistencePort branchPersistencePort;

    public ProductService(
            ProductPersistencePort productPersistencePort,
            BranchPersistencePort branchPersistencePort
    ) {
        this.productPersistencePort = productPersistencePort;
        this.branchPersistencePort = branchPersistencePort;
    }

    @Override
    public Mono<Product> execute(CreateProductCommand command) {
        BranchId branchId = BranchId.of(command.branchId());
        Name name = Name.of(command.name());
        Stock initialStock = Stock.of(command.initialStock());

        // Verificar que la sucursal existe
        return branchPersistencePort.existsById(branchId)
                .flatMap(exists -> {
                    // ✅ CORRECTO: Verificar si NO existe
                    if (!exists) {  // ← Debe ser !exists
                        return Mono.error(
                                new BranchNotFoundException(command.branchId())
                        );
                    }

                    // Crear y persistir producto
                    Product product = Product.create(branchId, name, initialStock);
                    return productPersistencePort.save(product);
                });
    }

    @Override
    public Mono<Product> execute(UpdateProductNameCommand command) {
        ProductId productId = ProductId.of(command.productId());
        Name newName = Name.of(command.newName());

        return productPersistencePort.findById(productId)
                .switchIfEmpty(Mono.error(
                        new ProductNotFoundException(command.productId())
                ))
                .doOnNext(product -> product.updateName(newName))
                .flatMap(productPersistencePort::save);
    }

    @Override
    public Mono<Product> execute(UpdateProductStockCommand command) {
        ProductId productId = ProductId.of(command.productId());
        Stock newStock = Stock.of(command.newStock());

        return productPersistencePort.findById(productId)
                .switchIfEmpty(Mono.error(
                        new ProductNotFoundException(command.productId())
                ))
                .doOnNext(product -> product.updateStock(newStock))
                .flatMap(productPersistencePort::save);
    }

    @Override
    public Mono<Void> execute(DeleteProductCommand command) {
        ProductId productId = ProductId.of(command.productId());

        // Verificar que existe antes de eliminar
        return productPersistencePort.existsById(productId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(
                                new ProductNotFoundException(command.productId())
                        );
                    }
                    return productPersistencePort.deleteById(productId);
                }).then();
    }

    @Override
    public Flux<TopStockProductResponse> execute(TopStockProductQuery query) {
        FranchiseId franchiseId = FranchiseId.of(query.franchiseId());

        // Obtener todas las sucursales de la franquicia
        return branchPersistencePort.findByFranchiseId(franchiseId)
                .collectList()
                .flatMapMany(branches -> {
                    if (branches.isEmpty()) {
                        return Flux.empty();
                    }

                    // Crear un mapa de BranchId -> Branch para acceso rápido
                    Map<BranchId, Branch> branchMap = branches.stream()
                            .collect(Collectors.toMap(Branch::getId, branch -> branch));

                    // Obtener todos los productos de la franquicia
                    return productPersistencePort.findByFranchiseId(franchiseId)
                            .collectList()
                            .flatMapMany(products -> {
                                if (products.isEmpty()) {
                                    return Flux.empty();
                                }

                                // Agrupar productos por sucursal y obtener el de mayor stock
                                Map<BranchId, Product> topProductsByBranch = products.stream()
                                        .collect(Collectors.groupingBy(
                                                Product::getBranchId,
                                                Collectors.collectingAndThen(
                                                        Collectors.maxBy(
                                                                Comparator.comparing(p -> p.getStock().getValue())
                                                        ),
                                                        opt -> opt.orElse(null)
                                                )
                                        ));

                                // Mapear a response
                                return Flux.fromIterable(topProductsByBranch.entrySet())
                                        .map(entry -> {
                                            BranchId branchId = entry.getKey();
                                            Product product = entry.getValue();
                                            Branch branch = branchMap.get(branchId);

                                            return new TopStockProductResponse(
                                                    branchId.getValue(),
                                                    branch.getName().getValue(),
                                                    product.getId().getValue(),
                                                    product.getName().getValue(),
                                                    product.getStock().getValue()
                                            );
                                        });
                            });
                });
    }
}