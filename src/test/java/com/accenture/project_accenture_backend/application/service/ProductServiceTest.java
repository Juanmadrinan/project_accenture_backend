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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductPersistencePort productPort;
    private BranchPersistencePort branchPort;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productPort = mock(ProductPersistencePort.class);
        branchPort = mock(BranchPersistencePort.class);
        productService = new ProductService(productPort, branchPort);
    }

    // -------------------------------------------------------------
    // CREATE PRODUCT
    // -------------------------------------------------------------
    @Test
    void createProduct_success() {
        CreateProductUseCase.CreateProductCommand command =
                new CreateProductUseCase.CreateProductCommand("branch-1", "Coca Cola", 10);

        BranchId branchId = BranchId.of("branch-1");
        Product product = Product.create(
                branchId,
                Name.of("Coca Cola"),
                Stock.of(10)
        );

        when(branchPort.existsById(branchId)).thenReturn(Mono.just(true));
        when(productPort.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.execute(command))
                .expectNextMatches(p ->
                        p.getName().getValue().equals("Coca Cola")
                                && p.getStock().getValue() == 10
                )
                .verifyComplete();

        verify(productPort, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_branchNotFound() {
        CreateProductUseCase.CreateProductCommand command =
                new CreateProductUseCase.CreateProductCommand("branch-404", "Sprite", 5);

        BranchId branchId = BranchId.of("branch-404");

        when(branchPort.existsById(branchId)).thenReturn(Mono.just(false));

        StepVerifier.create(productService.execute(command))
                .expectErrorMatches(err -> err instanceof BranchNotFoundException)
                .verify();

        verify(productPort, never()).save(any());
    }

    // -------------------------------------------------------------
    // UPDATE NAME
    // -------------------------------------------------------------
    @Test
    void updateProductName_success() {
        ProductId productId = ProductId.of("prod-1");

        Product product = Product.create(
                BranchId.of("branch-1"),
                Name.of("Old Name"),
                Stock.of(5)
        );

        UpdateProductNameUseCase.UpdateProductNameCommand command =
                new UpdateProductNameUseCase.UpdateProductNameCommand("prod-1", "New Name");

        when(productPort.findById(productId)).thenReturn(Mono.just(product));
        when(productPort.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.execute(command))
                .expectNextMatches(p -> p.getName().getValue().equals("New Name"))
                .verifyComplete();
    }

    @Test
    void updateProductName_notFound() {
        ProductId productId = ProductId.of("prod-x");
        UpdateProductNameUseCase.UpdateProductNameCommand command =
                new UpdateProductNameUseCase.UpdateProductNameCommand("prod-x", "Nuevo");

        when(productPort.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.execute(command))
                .expectError(ProductNotFoundException.class)
                .verify();
    }


    // -------------------------------------------------------------
    // UPDATE STOCK
    // -------------------------------------------------------------
    @Test
    void updateStock_success() {
        ProductId productId = ProductId.of("prod-10");

        Product product = Product.create(
                BranchId.of("branch-22"),
                Name.of("Pepsi"),
                Stock.of(5)
        );

        UpdateProductStockUseCase.UpdateProductStockCommand command =
                new UpdateProductStockUseCase.UpdateProductStockCommand("prod-10", 99);

        when(productPort.findById(productId)).thenReturn(Mono.just(product));
        when(productPort.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.execute(command))
                .expectNextMatches(p -> p.getStock().getValue() == 99)
                .verifyComplete();
    }

    @Test
    void updateStock_notFound() {
        ProductId productId = ProductId.of("prod-xx");

        UpdateProductStockUseCase.UpdateProductStockCommand command =
                new UpdateProductStockUseCase.UpdateProductStockCommand("prod-xx", 33);

        when(productPort.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.execute(command))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    // -------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------
    @Test
    void deleteProduct_success() {
        ProductId productId = ProductId.of("prod-del");

        when(productPort.existsById(productId)).thenReturn(Mono.just(true));
        when(productPort.deleteById(productId)).thenReturn(Mono.empty());

        DeleteProductUseCase.DeleteProductCommand command =
                new DeleteProductUseCase.DeleteProductCommand("prod-del");

        StepVerifier.create(productService.execute(command))
                .verifyComplete();

        verify(productPort, times(1)).deleteById(productId);
    }

    @Test
    void deleteProduct_notFound() {
        ProductId productId = ProductId.of("prod-nf");

        when(productPort.existsById(productId)).thenReturn(Mono.just(false));

        DeleteProductUseCase.DeleteProductCommand command =
                new DeleteProductUseCase.DeleteProductCommand("prod-nf");

        StepVerifier.create(productService.execute(command))
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productPort, never()).deleteById(any());
    }


    // -------------------------------------------------------------
    // GET TOP STOCK PER BRANCH
    // -------------------------------------------------------------
    @Test
    void getTopStockProduct_success() {

        FranchiseId franchiseId = FranchiseId.of("f1");

        Branch branchA = Branch.create(franchiseId, Name.of("Sucursal Norte"));
        Branch branchB = Branch.create(franchiseId, Name.of("Sucursal Sur"));

        when(branchPort.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchA, branchB));

        Product p1 = Product.create(branchA.getId(), Name.of("Manzanas"), Stock.of(100));
        Product p2 = Product.create(branchA.getId(), Name.of("Peras"), Stock.of(50));
        Product p3 = Product.create(branchB.getId(), Name.of("Café"), Stock.of(200));

        when(productPort.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(p1, p2, p3));

        GetTopStockProductsByBranchUseCase.TopStockProductQuery query = new GetTopStockProductsByBranchUseCase.TopStockProductQuery("f1");

        StepVerifier.create(productService.execute(query))
                .expectNextCount(2) // A y B
                .verifyComplete();
    }
}
