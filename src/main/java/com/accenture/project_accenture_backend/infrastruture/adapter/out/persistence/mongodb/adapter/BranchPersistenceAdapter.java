package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.adapter;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.port.out.BranchPersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.BranchEntity;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.mapper.BranchMapper;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository.BranchReactiveRepository;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.repository.FranchiseReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BranchPersistenceAdapter implements BranchPersistencePort {

    private final BranchMapper branchMapper;
    private final BranchReactiveRepository branchReactiveRepository;

    public BranchPersistenceAdapter(BranchMapper branchMapper, BranchReactiveRepository branchReactiveRepository) {
        this.branchMapper = branchMapper;
        this.branchReactiveRepository = branchReactiveRepository;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return Mono.just(branch)
                .map(branchMapper::toEntity)
                .flatMap(branchReactiveRepository::save)
                .map(branchMapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(BranchId branchId) {
        return branchReactiveRepository.findById(branchId.getValue()).map(branchMapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(FranchiseId franchiseId) {
        return branchReactiveRepository.findByFranchiseId(franchiseId.getValue()).map(branchMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(BranchId branchId) {
        return branchReactiveRepository.existsById(branchId.getValue());
    }
}
