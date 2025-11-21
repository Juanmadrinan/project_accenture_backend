package com.accenture.project_accenture_backend.infrastruture.adapter.in.rest.dto.response;

public record TopStockProductResponse(
        String branchId,
        String branchName,
        String productId,
        String productName,
        Integer stock
) {
}
