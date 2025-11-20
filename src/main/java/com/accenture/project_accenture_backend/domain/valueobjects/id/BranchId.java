package com.accenture.project_accenture_backend.domain.valueobjects.id;

import java.util.Objects;
import java.util.UUID;

public final class BranchId {
    private final String value;

    public BranchId(String value) {
        this.value = value;
    }

    public static BranchId of(String value) {
        if(value ==  null || value.isBlank()) {
            throw new IllegalArgumentException("Branch id cannot be null or blank");
        }
        return new BranchId(value);
    }

    public static BranchId generate(){
        return new BranchId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchId branchId = (BranchId) o;
        return Objects.equals(value, branchId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
