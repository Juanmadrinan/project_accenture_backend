package com.accenture.project_accenture_backend.domain.port.in.franchise;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import reactor.core.publisher.Mono;

public interface UpdateFranchiseNameUseCase {
    Mono<Franchise> execute(UpdateFranchiseNameCommand command);

    record UpdateFranchiseNameCommand(String franchiseId, String newName) {
        public UpdateFranchiseNameCommand{
            if(franchiseId == null || newName == null){
                throw new IllegalArgumentException("name is null or blank");

            }
            if(franchiseId.isBlank() || newName.isBlank()){
                throw new IllegalArgumentException("name is null or blank");
            }
        }
    }
}
