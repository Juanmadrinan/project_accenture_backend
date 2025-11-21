package com.accenture.project_accenture_backend.domain.exception;

public class FranchiseNotFoundException extends DomainException {
    public FranchiseNotFoundException(String franchiseId) {
        super(String.format("Franchise with id not found", franchiseId));
    }

}
