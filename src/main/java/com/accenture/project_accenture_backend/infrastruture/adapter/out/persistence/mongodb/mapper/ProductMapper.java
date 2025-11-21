package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.mapper;

import com.accenture.project_accenture_backend.domain.entities.Product;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.Stock;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.ProductId;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
          product.getId().getValue(),
          product.getBranchId().getValue(),
          product.getName().getValue(),
          product.getStock().getValue(),
          product.getCreatedAt(),
          product.getUpdatedAt()
        );
    }

    public Product toDomain(ProductEntity entity) {
        return Product.reconstitute(
                ProductId.of(entity.getId()),
                BranchId.of(entity.getBranchId()),
                entity.getCreatedAt(),
                Name.of(entity.getName()),
                Stock.of(entity.getStock()),
                entity.getUpdatedAt()
        );
    }
}
