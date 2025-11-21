package com.accenture.project_accenture_backend.application.service;

import com.accenture.project_accenture_backend.domain.entities.Franchise;
import com.accenture.project_accenture_backend.domain.exception.FranchiseNotFoundException;
import com.accenture.project_accenture_backend.domain.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.project_accenture_backend.domain.port.in.franchise.UpdateFranchiseNameUseCase;
import com.accenture.project_accenture_backend.domain.port.out.FranchisePersistencePort;
import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FranchiseServiceTest {

    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private FranchiseService franchiseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------
    // TEST 1: Crear franquicia
    // ------------------------------------------------------
    @Test
    void createFranchise_ShouldReturnSavedFranchise() {
        // Arrange
        CreateFranchiseUseCase.CreateFranchiseCommand command = new CreateFranchiseUseCase.CreateFranchiseCommand("Burger King");
        Franchise savedFranchise = Franchise.create(Name.of("Burger King"));

        when(persistencePort.save(any(Franchise.class)))
                .thenReturn(Mono.just(savedFranchise));

        // Act + Assert
        StepVerifier.create(franchiseService.execute(command))
                .expectNextMatches(franchise ->
                        franchise.getName().getValue().equals("Burger King")
                )
                .verifyComplete();

        verify(persistencePort, times(1)).save(any());
    }

    // ------------------------------------------------------
    // TEST 2: Actualizar nombre de franquicia
    // ------------------------------------------------------
    @Test
    void updateFranchiseName_ShouldUpdateAndReturnFranchise() {
        // Arrange
        UpdateFranchiseNameUseCase.UpdateFranchiseNameCommand command =
                new UpdateFranchiseNameUseCase.UpdateFranchiseNameCommand("123", "NewName");

        FranchiseId id = FranchiseId.of("123");
        Franchise oldFranchise = Franchise.create(Name.of("OldName"));

        when(persistencePort.findById(id))
                .thenReturn(Mono.just(oldFranchise));

        when(persistencePort.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act + Assert
        StepVerifier.create(franchiseService.execute(command))
                .expectNextMatches(franchise ->
                        franchise.getName().getValue().equals("NewName")
                )
                .verifyComplete();

        verify(persistencePort).findById(id);
        verify(persistencePort).save(any());
    }

    // ------------------------------------------------------
    // TEST 3: Actualizar nombre cuando no existe la franquicia
    // ------------------------------------------------------
    @Test
    void updateFranchiseName_ShouldReturnError_WhenNotFound() {
        UpdateFranchiseNameUseCase.UpdateFranchiseNameCommand command =
                new UpdateFranchiseNameUseCase.UpdateFranchiseNameCommand("999", "Test");

        FranchiseId id = FranchiseId.of("999");

        when(persistencePort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.execute(command))
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(persistencePort).findById(id);
        verify(persistencePort, never()).save(any());
    }

    // ------------------------------------------------------
    // TEST 4: Obtener franquicia por ID
    // ------------------------------------------------------
    @Test
    void getFranchise_ShouldReturnFranchise_WhenExists() {
        FranchiseId id = FranchiseId.of("123");
        Franchise franchise = Franchise.create(Name.of("Pizza Hut"));

        when(persistencePort.findById(id))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseService.execute(id))
                .expectNext(franchise)
                .verifyComplete();

        verify(persistencePort).findById(id);
    }

    // ------------------------------------------------------
    // TEST 5: Obtener franquicia por ID cuando no existe
    // ------------------------------------------------------
    @Test
    void getFranchise_ShouldReturnError_WhenNotFound() {
        FranchiseId id = FranchiseId.of("999");

        when(persistencePort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.execute(id))
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(persistencePort).findById(id);
    }
}
