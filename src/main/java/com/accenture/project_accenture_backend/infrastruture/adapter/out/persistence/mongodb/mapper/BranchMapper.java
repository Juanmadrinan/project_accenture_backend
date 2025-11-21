package com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.mapper;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.infrastruture.adapter.out.persistence.mongodb.entity.BranchEntity;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {
    public BranchEntity toEntity(Branch branch) {
        return new BranchEntity(
                branch.getId().getValue(),
                branch.getFranchiseId().getValue(),
                branch.getName().getValue(),
                branch.getCreatedAt(),
                branch.getUpdatedAt()
        );
    }

    public Branch toDomain(BranchEntity branchEntity) {
        return Branch.reconstitute(
                BranchId.of(branchEntity.getId()),
                FranchiseId.of(branchEntity.getFranchiseId()),
                Name.of(branchEntity.getName()),
                branchEntity.getCreatedAt(),
                branchEntity.getUpdatedAt()
        );
    }
}
