package com.accenture.project_accenture_backend.domain.port.out;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(FranchiseId franchiseId);
    Mono<Boolean> existsById(FranchiseId franchiseId);
}
