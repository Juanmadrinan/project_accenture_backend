package com.accenture.project_accenture_backend.domain.port.out;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchPersistencePort {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(BranchId branchId);
    Flux<Branch> findByFranchiseId(FranchiseId franchiseId);
    Mono<Boolean> existsById(BranchId branchId);
}
