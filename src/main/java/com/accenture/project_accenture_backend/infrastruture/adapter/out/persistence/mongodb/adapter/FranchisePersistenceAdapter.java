package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.adapter;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.port.out.FranchisePersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.mapper.FranchiseMapper;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository.FranchiseReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FranchisePersistenceAdapter implements FranchisePersistencePort {
    private final FranchiseMapper franchiseMapper;
    private final FranchiseReactiveRepository franchiseReactiveRepository;

    public FranchisePersistenceAdapter(FranchiseMapper franchiseMapper, FranchiseReactiveRepository franchiseReactiveRepository) {
        this.franchiseMapper = franchiseMapper;
        this.franchiseReactiveRepository = franchiseReactiveRepository;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return Mono.just(franchise)
                .map(franchiseMapper::toEntity)
                .flatMap(franchiseReactiveRepository::save)
                .map(franchiseMapper::toDomain);
    }
    @Override
    public Mono<Franchise> findById(FranchiseId franchiseId) {
        return franchiseReactiveRepository
                .findById(franchiseId.getValue())
                .map(franchiseMapper::toDomain);
    }
    @Override
    public Mono<Boolean> existsById(FranchiseId franchiseId) {
        return franchiseReactiveRepository.existsById(franchiseId.getValue());
    }

}
