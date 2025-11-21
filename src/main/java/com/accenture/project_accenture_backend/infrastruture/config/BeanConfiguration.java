package com.accenture.project_accenture_backend.infrastruture.config;

import com.accenture.project_accenture_backend.application.service.BranchService;
import com.accenture.project_accenture_backend.application.service.FranchiseService;
import com.accenture.project_accenture_backend.application.service.ProductService;
import com.accenture.project_accenture_backend.domain.port.in.branch.CreateBranchUseCase;
import com.accenture.project_accenture_backend.domain.port.in.branch.GetBranchUseCase;
import com.accenture.project_accenture_backend.domain.port.in.branch.UpdateBranchNameUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.GetFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.UpdateFranchiseNameUseCase;
import com.accenture.project_accenture_backend.domain.port.in.product.*;
import com.accenture.project_accenture_backend.domain.port.out.BranchPersistencePort;
import com.accenture.project_accenture_backend.domain.port.out.FranchisePersistencePort;
import com.accenture.project_accenture_backend.domain.port.out.ProductPersistencePort;
import org.springframework.context.annotation.Bean;

public class BeanConfiguration {

    // Franchise

    @Bean
    public FranchiseService franchiseService(FranchisePersistencePort franchisePersistencePort) {
        return new FranchiseService(franchisePersistencePort);
    }

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchiseService franchiseService) {
        return franchiseService;
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(FranchiseService franchiseService) {
        return franchiseService;
    }

    @Bean
    public GetFranchiseUseCase getFranchiseUseCase(FranchiseService franchiseService) {
        return franchiseService;
    }

    // Branch

    @Bean
    public BranchService branchService(BranchPersistencePort branchPersistencePort, FranchisePersistencePort franchisePersistencePort) {
        return new BranchService(branchPersistencePort, franchisePersistencePort);
    }

    @Bean
    public CreateBranchUseCase createBranchUseCase(BranchService branchService) {
        return branchService;
    }

    @Bean
    public UpdateBranchNameUseCase updateBranchNameUseCase(BranchService branchService) {
        return branchService;
    }

    @Bean
    public GetBranchUseCase getBranchUseCase(BranchService branchService) {
        return branchService;
    }

    @Bean
    public ProductService productService(
            ProductPersistencePort productPersistencePort,
            BranchPersistencePort branchPersistencePort
    ) {
        return new ProductService(productPersistencePort, branchPersistencePort);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductService productService) {
        return productService;
    }

    @Bean
    public UpdateProductNameUseCase updateProductNameUseCase(ProductService productService) {
        return productService;
    }

    @Bean
    public UpdateProductStockUseCase updateProductStockUseCase(ProductService productService) {
        return productService;
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductService productService) {
        return productService;
    }

    @Bean
    public GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase(ProductService productService) {
        return productService;
    }
}
