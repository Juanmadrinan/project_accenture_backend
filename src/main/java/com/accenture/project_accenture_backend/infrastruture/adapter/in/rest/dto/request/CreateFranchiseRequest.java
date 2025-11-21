package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFranchiseRequest(
        @NotBlank(message = "NotBlank")
        @Size(min = 1, max = 100, message = "The name must be between 3 and 100 characters long.")
        String name
) {
}
