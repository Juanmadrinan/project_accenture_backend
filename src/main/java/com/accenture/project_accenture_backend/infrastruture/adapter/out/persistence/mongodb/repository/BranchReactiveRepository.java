package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository;

import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.BranchEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BranchReactiveRepository extends ReactiveMongoRepository<BranchEntity, String> {
    Flux<BranchEntity> findByFranchiseId(String franchiseId);
}
