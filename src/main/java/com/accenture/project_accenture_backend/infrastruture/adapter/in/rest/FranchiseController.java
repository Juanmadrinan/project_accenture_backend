package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.GetFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.UpdateFranchiseNameUseCase;

import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.CreateFranchiseRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request.UpdateFranchiseNameRequest;
import com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response.FranchiseResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final GetFranchiseUseCase getFranchiseUseCase;

    public FranchiseController(
            CreateFranchiseUseCase createFranchiseUseCase,
            UpdateFranchiseNameUseCase updateFranchiseNameUseCase,
            GetFranchiseUseCase getFranchiseUseCase
    ) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
        this.getFranchiseUseCase = getFranchiseUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(
            @RequestBody @Valid CreateFranchiseRequest request
    ) {
        CreateFranchiseUseCase.CreateFranchiseCommand command = new CreateFranchiseUseCase.CreateFranchiseCommand(request.name());

        return createFranchiseUseCase.execute(command)
                .map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<FranchiseResponse> getFranchise(@PathVariable String id) {
        FranchiseId franchiseId = FranchiseId.of(id);

        return getFranchiseUseCase.execute(franchiseId)
                .map(this::toResponse);
    }

    @PutMapping("/{id}/name")
    public Mono<FranchiseResponse> updateFranchiseName(
            @PathVariable String id,
            @RequestBody @Valid UpdateFranchiseNameRequest request
    ) {
        // Crear Command desde el DTO
        UpdateFranchiseNameUseCase.UpdateFranchiseNameCommand command = new UpdateFranchiseNameUseCase.UpdateFranchiseNameCommand(
                id,
                request.name()
        );

        return updateFranchiseNameUseCase.execute(command)
                .map(this::toResponse);
    }

    /**
     * Mapper: Domain Entity → Response DTO
     */
    private FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(
                franchise.getId().getValue(),
                franchise.getName().getValue(),
                franchise.getCreatedAt(),
                franchise.getUpdatedAt()
        );
    }
}
