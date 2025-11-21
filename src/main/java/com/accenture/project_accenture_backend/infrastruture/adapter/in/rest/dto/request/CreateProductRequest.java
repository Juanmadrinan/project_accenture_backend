package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request;

import jakarta.validation.constraints.*;

public record CreateProductRequest(
        @NotBlank(message = "The branch ID cannot be empty.")
        String branchId,

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters long.")
        String name,

        @NotNull(message = "El stock inicial no puede ser nulo")
        @Min(value = 0, message = "El stock inicial no puede ser negativo")
        Integer initialStock
) {}