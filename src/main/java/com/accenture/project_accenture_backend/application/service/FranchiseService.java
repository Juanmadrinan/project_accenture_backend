package com.accenture.project_accenture_backend.application.service;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.exception.FranchiseNotFoundException;
import com.accenture.project_accenture_backend.domain.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.GetFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.UpdateFranchiseNameUseCase;
import com.accenture.project_accenture_backend.domain.port.out.FranchisePersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FranchiseService implements CreateFranchiseUseCase, UpdateFranchiseNameUseCase, GetFranchiseUseCase {

    private final FranchisePersistencePort persistencePort;

    public FranchiseService(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Franchise> execute(CreateFranchiseCommand command) {
        return Mono.fromCallable(() -> {
                Name name = Name.of(command.name());

                return Franchise.create(name);
                })
                .flatMap(persistencePort::save);
    }

    @Override
    public Mono<Franchise> execute(UpdateFranchiseNameCommand command) {
        FranchiseId franchiseId = FranchiseId.of(command.franchiseId());
        Name newName = Name.of(command.newName());

        return persistencePort.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(command.franchiseId())
                ))
                .doOnNext(franchise -> franchise.updateName(newName))
                .flatMap(persistencePort::save);
    }

    @Override
    public Mono<Franchise> execute(FranchiseId franchiseId) {
        return persistencePort.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseNotFoundException(franchiseId.getValue())
                ));
    }
}
