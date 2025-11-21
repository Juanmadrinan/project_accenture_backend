package com.accenture.project_accenture_backend.domain.exception;

public class BranchNotFoundException extends DomainException {
    public BranchNotFoundException(String branchId) {
        super(String.format("Branch with id not found", branchId));
    }

}
