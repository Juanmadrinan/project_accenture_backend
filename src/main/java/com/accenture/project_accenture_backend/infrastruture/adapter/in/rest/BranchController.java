package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.port.in.branch.CreateBranchUseCase;
import com.accenture.project_accenture_backend.domain.port.in.branch.GetBranchUseCase;
import com.accenture.project_accenture_backend.domain.port.in.branch.UpdateBranchNameUseCase;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.CreateBranchRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.UpdateBranchNameRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response.BranchResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final GetBranchUseCase getBranchUseCase;

    public BranchController(
            CreateBranchUseCase createBranchUseCase,
            UpdateBranchNameUseCase updateBranchNameUseCase,
            GetBranchUseCase getBranchUseCase
    ) {
        this.createBranchUseCase = createBranchUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
        this.getBranchUseCase = getBranchUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> createBranch(
            @RequestBody @Valid CreateBranchRequest request

    ) {
        CreateBranchUseCase.CreateBranchCommand command = new CreateBranchUseCase.CreateBranchCommand(
                request.franchiseId(),
                request.name()
        );

        return createBranchUseCase.execute(command)
                .map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<BranchResponse> getBranch(@PathVariable String id) {
        BranchId branchId = BranchId.of(id);

        return getBranchUseCase.execute(branchId)
                .map(this::toResponse);
    }

    @PutMapping("/{id}/name")
    public Mono<BranchResponse> updateBranchName(
            @PathVariable String id,
            @RequestBody @Valid UpdateBranchNameRequest request
    ) {
        UpdateBranchNameUseCase.UpdateBranchNameCommand command = new UpdateBranchNameUseCase.UpdateBranchNameCommand(
                id,
                request.name()
        );

        return updateBranchNameUseCase.execute(command)
                .map(this::toResponse);
    }

    /**
     * Mapper: Domain Entity → Response DTO
     */
    private BranchResponse toResponse(Branch branch) {
        return new BranchResponse(
                branch.getId().getValue(),
                branch.getFranchiseId().getValue(),
                branch.getName().getValue(),
                branch.getCreatedAt(),
                branch.getUpdatedAt()
        );
    }
}
