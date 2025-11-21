package com.accenture.project_accenture_backend.application.service;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.exception.BranchNotFoundException;
import com.accenture.project_accenture_backend.domain.exception.FranchiseNotFoundException;
import com.accenture.project_accenture_backend.domain.port.in.branch.CreateBranchUseCase;
import com.accenture.project_accenture_backend.domain.port.in.branch.GetBranchUseCase;
import com.accenture.project_accenture_backend.domain.port.in.branch.UpdateBranchNameUseCase;
import com.accenture.project_accenture_backend.domain.port.out.BranchPersistencePort;
import com.accenture.project_accenture_backend.domain.port.out.FranchisePersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BranchService implements
        CreateBranchUseCase,
        UpdateBranchNameUseCase,
        GetBranchUseCase {

    private final BranchPersistencePort branchPersistencePort;
    private final FranchisePersistencePort franchisePersistencePort;

    public BranchService(
            BranchPersistencePort branchPersistencePort,
            FranchisePersistencePort franchisePersistencePort
    ) {
        this.branchPersistencePort = branchPersistencePort;
        this.franchisePersistencePort = franchisePersistencePort;
    }

    @Override
    public Mono<Branch> execute(CreateBranchCommand command) {
        FranchiseId franchiseId = FranchiseId.of(command.franchiseId());
        Name name = Name.of(command.name());

        return franchisePersistencePort.existsById(franchiseId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(
                                new FranchiseNotFoundException(command.franchiseId())
                        );
                    }

                    Branch branch = Branch.create(franchiseId, name);
                    return branchPersistencePort.save(branch);
                });
    }

    @Override
    public Mono<Branch> execute(UpdateBranchNameCommand command) {
        BranchId branchId = BranchId.of(command.branchId());
        Name newName = Name.of(command.newName());

        return branchPersistencePort.findById(branchId)
                .switchIfEmpty(Mono.error(
                        new BranchNotFoundException(command.branchId())
                ))
                .doOnNext(branch -> branch.updateName(newName))
                .flatMap(branchPersistencePort::save);
    }

    @Override
    public Mono<Branch> execute(BranchId branchId) {
        return branchPersistencePort.findById(branchId)
                .switchIfEmpty(Mono.error(
                        new BranchNotFoundException(branchId.getValue())
                ));
    }
}
