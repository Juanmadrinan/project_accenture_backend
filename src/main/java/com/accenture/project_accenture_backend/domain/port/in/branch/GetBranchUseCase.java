package com.accenture.project_accenture_backend.domain.port.in.branch;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import reactor.core.publisher.Mono;

public interface GetBranchUseCase {
    Mono<Branch> execute(BranchId branchId);
}
