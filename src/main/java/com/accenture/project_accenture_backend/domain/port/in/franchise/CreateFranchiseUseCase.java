package com.accenture.project_accenture_backend.domain.port.in.franchise;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import reactor.core.publisher.Mono;

public interface CreateFranchiseUseCase {
    Mono<Franchise> execute(CreateFranchiseCommand commannd);

    record CreateFranchiseCommand(String name) {
        public CreateFranchiseCommand {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name is null or blank");
            }
        }
    }
}

