package com.accenture.project_accenture_backend.domain.port.in.product;

import reactor.core.publisher.Flux;

public interface GetTopStockProductsByBranchUseCase {

    Flux<TopStockProductResponse> execute(TopStockProductQuery query);

    record TopStockProductQuery(String franchiseId) {
        public TopStockProductQuery {
            if (franchiseId == null || franchiseId.isBlank()) {
                throw new IllegalArgumentException("FranchiseId is null or blank");
            }
        }
    }

    record TopStockProductResponse(
            String branchId,
            String branchName,
            String productId,
            String productName,
            int stock
    ) {}
}
