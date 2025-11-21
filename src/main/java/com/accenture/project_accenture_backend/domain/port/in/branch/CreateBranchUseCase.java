package com.accenture.project_accenture_backend.domain.port.in.branch;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.exception.BranchNotFoundException;
import com.accenture.project_accenture_backend.domain.exception.FranchiseNotFoundException;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import reactor.core.publisher.Mono;

public interface CreateBranchUseCase {
    Mono<Branch> execute(CreateBranchCommand command);

    record CreateBranchCommand(String franchiseId, String name){
        public CreateBranchCommand{
            if (franchiseId == null || franchiseId.isBlank()) {
                throw new IllegalArgumentException("FranchiseId null or blanck");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name null or blank");
            }
        }
    }
}
