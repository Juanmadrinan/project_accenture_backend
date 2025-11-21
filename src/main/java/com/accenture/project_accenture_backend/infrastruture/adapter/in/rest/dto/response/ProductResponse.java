package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response;

import java.time.Instant;

public record ProductResponse(
        String id,
        String branchId,
        String name,
        Integer stock,
        Instant createdAt,
        Instant updatedAt
) {
}
