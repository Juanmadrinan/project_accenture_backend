package com.accenture.project_accenture_backend.domain.port.in.franchise;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import reactor.core.publisher.Mono;

public interface GetFranchiseUseCase {
    Mono<Franchise> execute(FranchiseId franchiseId);
}
