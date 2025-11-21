package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository;

import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.ProductEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ProductReactiveRepository extends ReactiveMongoRepository<ProductEntity, String> {
    Flux<ProductEntity> findByBranchId(String branchId);


    /*Buscando todo los productos de una franquicia (por medio de sus sucursales)*/
    @Query("{ 'branchId': { $in: ?0 } }")
    Flux<ProductEntity> findByBranchIdIn(List<String> branchIds);

}
