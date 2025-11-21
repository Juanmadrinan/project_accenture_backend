package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.mapper;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.BranchEntity;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

@Component
public class FranchiseMapper {
    public FranchiseEntity toEntity(Franchise franchise) {
        return new FranchiseEntity(
                franchise.getId().getValue(),
                franchise.getName().getValue(),
                franchise.getCreatedAt(),
                franchise.getUpdatedAt()
        );
    }

    public Franchise toDomain(FranchiseEntity entity) {
        return Franchise.reconstitute(
                FranchiseId.of(entity.getId()),
                entity.getUpdatedAt(),
                entity.getCreatedAt(),
                Name.of(entity.getName())
        );
    }
}
