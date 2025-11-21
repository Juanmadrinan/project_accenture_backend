package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository;

import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseReactiveRepository extends ReactiveMongoRepository<FranchiseEntity, String> {
}
