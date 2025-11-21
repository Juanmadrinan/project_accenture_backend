package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response;

import java.time.Instant;

public record FranchiseResponse(
        String id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
