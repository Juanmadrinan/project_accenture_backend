package com.accenture.project_accenture_backend.application.service;

import com.accenture.project_accenture_backend.domain.entities.Branch;
import com.accenture.project_accenture_backend.domain.exception.BranchNotFoundException;
import com.accenture.project_accenture_backend.domain.exception.FranchiseNotFoundException;
import com.accenture.project_accenture_backend.domain.port.in.branch.CreateBranchUseCase.CreateBranchCommand;
import com.accenture.project_accenture_backend.domain.port.in.branch.UpdateBranchNameUseCase.UpdateBranchNameCommand;
import com.accenture.project_accenture_backend.domain.port.out.BranchPersistencePort;
import com.accenture.project_accenture_backend.domain.port.out.FranchisePersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class BranchServiceTest {

    @Mock
    private BranchPersistencePort branchPersistencePort;

    @Mock
    private FranchisePersistencePort franchisePersistencePort;

    @InjectMocks
    private BranchService branchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------
    // TEST 1: Crear sucursal
    // ------------------------------------------------------
    @Test
    void createBranch_ShouldReturnSavedBranch() {
        // Arrange
        CreateBranchCommand command = new CreateBranchCommand("123", "Sucursal A");

        FranchiseId franchiseId = FranchiseId.of("123");
        Branch branch = Branch.create(franchiseId, Name.of("Sucursal A"));

        when(franchisePersistencePort.existsById(franchiseId))
                .thenReturn(Mono.just(false)); // La franquicia sí existe según tu lógica actual

        when(branchPersistencePort.save(any(Branch.class)))
                .thenReturn(Mono.just(branch));

        // Act & Assert
        StepVerifier.create(branchService.execute(command))
                .expectNextMatches(result ->
                        result.getName().getValue().equals("Sucursal A"))
                .verifyComplete();

        verify(franchisePersistencePort).existsById(franchiseId);
        verify(branchPersistencePort).save(any(Branch.class));
    }

    // ------------------------------------------------------
    // TEST 2: Crear sucursal cuando franquicia no existe
    // ------------------------------------------------------
    @Test
    void createBranch_ShouldThrowError_WhenFranchiseNotFound() {
        CreateBranchCommand command = new CreateBranchCommand("999","Sucursal X");
        FranchiseId franchiseId = FranchiseId.of("999");

        when(franchisePersistencePort.existsById(franchiseId))
                .thenReturn(Mono.just(true)); // según tu lógica, true = error

        StepVerifier.create(branchService.execute(command))
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchisePersistencePort).existsById(franchiseId);
        verify(branchPersistencePort, never()).save(any());
    }

    // ------------------------------------------------------
    // TEST 3: Actualizar nombre de sucursal
    // ------------------------------------------------------
    @Test
    void updateBranchName_ShouldUpdateAndReturnBranch() {
        UpdateBranchNameCommand command =
                new UpdateBranchNameCommand("321", "Nuevo Nombre");

        BranchId branchId = BranchId.of("321");
        Branch existingBranch = Branch.create(
                FranchiseId.of("111"),
                Name.of("Viejo Nombre")
        );

        when(branchPersistencePort.findById(branchId))
                .thenReturn(Mono.just(existingBranch));

        when(branchPersistencePort.save(any(Branch.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(branchService.execute(command))
                .expectNextMatches(branch ->
                        branch.getName().getValue().equals("Nuevo Nombre"))
                .verifyComplete();

        verify(branchPersistencePort).findById(branchId);
        verify(branchPersistencePort).save(any());
    }

    // ------------------------------------------------------
    // TEST 4: Actualizar nombre cuando sucursal no existe
    // ------------------------------------------------------
    @Test
    void updateBranchName_ShouldReturnError_WhenNotFound() {
        UpdateBranchNameCommand command =
                new UpdateBranchNameCommand("000", "Nuevo Nombre");

        BranchId branchId = BranchId.of("000");

        when(branchPersistencePort.findById(branchId))
                .thenReturn(Mono.empty());

        StepVerifier.create(branchService.execute(command))
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchPersistencePort).findById(branchId);
        verify(branchPersistencePort, never()).save(any());
    }

    // ------------------------------------------------------
    // TEST 5: Obtener sucursal por ID
    // ------------------------------------------------------
    @Test
    void getBranch_ShouldReturnBranch_WhenExists() {
        BranchId branchId = BranchId.of("100");
        Branch branch = Branch.create(
                FranchiseId.of("1"),
                Name.of("Central")
        );

        when(branchPersistencePort.findById(branchId))
                .thenReturn(Mono.just(branch));

        StepVerifier.create(branchService.execute(branchId))
                .expectNext(branch)
                .verifyComplete();

        verify(branchPersistencePort).findById(branchId);
    }

    // ------------------------------------------------------
    // TEST 6: Obtener sucursal por ID cuando no existe
    // ------------------------------------------------------
    @Test
    void getBranch_ShouldReturnError_WhenNotFound() {
        BranchId branchId = BranchId.of("404");

        when(branchPersistencePort.findById(branchId))
                .thenReturn(Mono.empty());

        StepVerifier.create(branchService.execute(branchId))
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchPersistencePort).findById(branchId);
    }
}
