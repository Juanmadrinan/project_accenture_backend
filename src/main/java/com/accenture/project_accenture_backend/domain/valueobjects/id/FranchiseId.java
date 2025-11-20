package com.accenture.project_accenture_backend.domain.valueobjects.id;

import java.util.Objects;
import java.util.UUID;

public final class FranchiseId {
    private final String value;

    private FranchiseId(String value) {
        this.value = value;
    }

    public static FranchiseId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Franchise Id cannot be null or blank");
        }
        return new FranchiseId(value.trim());
    }

    public static FranchiseId generate() {
        return new FranchiseId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FranchiseId franchiseId = (FranchiseId) o;
        return Objects.equals(value, franchiseId.value); /*RECORDATORIO TENER CUIDADO*/
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
