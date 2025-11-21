package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest;

import com.accenture.project_accenture_backend.domain.entities.Product;
import com.accenture.project_accenture_backend.domain.port.in.product.*;
import com.accenture.project_accenture_backend.domain.port.in.product.CreateProductUseCase.CreateProductCommand;
import com.accenture.project_accenture_backend.domain.port.in.product.DeleteProductUseCase.DeleteProductCommand;
import com.accenture.project_accenture_backend.domain.port.in.product.GetTopStockProductsByBranchUseCase.TopStockProductQuery;
import com.accenture.project_accenture_backend.domain.port.in.product.UpdateProductNameUseCase.UpdateProductNameCommand;
import com.accenture.project_accenture_backend.domain.port.in.product.UpdateProductStockUseCase.UpdateProductStockCommand;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.CreateProductRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.UpdateProductNameRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.UpdateProductStockRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response.ProductResponse;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response.TopStockProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            UpdateProductNameUseCase updateProductNameUseCase,
            UpdateProductStockUseCase updateProductStockUseCase,
            DeleteProductUseCase deleteProductUseCase,
            GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductNameUseCase = updateProductNameUseCase;
        this.updateProductStockUseCase = updateProductStockUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getTopStockProductsByBranchUseCase = getTopStockProductsByBranchUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> createProduct(
            @RequestBody @Valid CreateProductRequest request
    ) {
        CreateProductCommand command = new CreateProductCommand(
                request.branchId(),
                request.name(),
                request.initialStock()
        );

        return createProductUseCase.execute(command)
                .map(this::toResponse);
    }

    @PutMapping("/{id}/name")
    public Mono<ProductResponse> updateProductName(
            @PathVariable String id,
            @RequestBody @Valid UpdateProductNameRequest request
    ) {
        UpdateProductNameCommand command = new UpdateProductNameCommand(
                id,
                request.name()
        );

        return updateProductNameUseCase.execute(command)
                .map(this::toResponse);
    }

    @PutMapping("/{id}/stock")
    public Mono<ProductResponse> updateProductStock(
            @PathVariable String id,
            @RequestBody @Valid UpdateProductStockRequest request
    ) {
        UpdateProductStockCommand command = new UpdateProductStockCommand(
                id,
                request.stock()
        );

        return updateProductStockUseCase.execute(command)
                .map(this::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String id) {
        DeleteProductCommand command = new DeleteProductCommand(id);

        return deleteProductUseCase.execute(command);
    }

    @GetMapping("/top-stock/franchise/{franchiseId}")
    public Flux<TopStockProductResponse> getTopStockProductsByBranch(
            @PathVariable String franchiseId
    ) {
        TopStockProductQuery query = new TopStockProductQuery(franchiseId);

        return getTopStockProductsByBranchUseCase.execute(query)
                .map(domainResponse -> new TopStockProductResponse(
                        domainResponse.branchId(),
                        domainResponse.branchName(),
                        domainResponse.productId(),
                        domainResponse.productName(),
                        domainResponse.stock()
                ));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId().getValue(),
                product.getBranchId().getValue(),
                product.getName().getValue(),
                product.getStock().getValue(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
