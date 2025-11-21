package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response;

import java.time.Instant;

public record BranchResponse(
        String id,
        String franchiseId,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
