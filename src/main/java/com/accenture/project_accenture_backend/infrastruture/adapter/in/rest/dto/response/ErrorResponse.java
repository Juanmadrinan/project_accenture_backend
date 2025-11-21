package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response;

import java.time.Instant;

public record ErrorResponse(
        String message,
        String error,
        Integer status,
        Instant timestamp
) {
}
