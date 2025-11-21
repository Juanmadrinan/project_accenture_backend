package com.accenture.project_accenture_backend.domain.port.in.branch;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import reactor.core.publisher.Mono;

public interface UpdateBranchNameUseCase {

    Mono<Branch> execute(UpdateBranchNameCommand command);

    record UpdateBranchNameCommand(String branchId, String newName) {
        public UpdateBranchNameCommand {
            if (branchId == null || branchId.isBlank()) {
                throw new IllegalArgumentException("BranchId is null or blank");
            }
            if (newName == null || newName.isBlank()) {
                throw new IllegalArgumentException("name is null or blank");
            }
        }
    }
}
