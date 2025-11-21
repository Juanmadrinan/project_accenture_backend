package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStockRequest(

        @NotNull(message = "The stock cannot be null")
        @Min(value = 0, message = "The stock cannot be negative")
        Integer stock
) {}