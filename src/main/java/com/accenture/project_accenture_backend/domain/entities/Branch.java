package com.accenture.project_accenture_backend.domain.entities;

import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;

import java.time.Instant;
import java.util.Objects;

public class Branch {
    private final BranchId id;
    private final FranchiseId franchiseId;
    private Name name;
    private final Instant createdAt;
    private Instant updatedAt;

    public Branch(BranchId id, FranchiseId franchiseId, Name name, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.franchiseId = franchiseId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Branch create(FranchiseId franchiseId, Name name) {
        Objects.requireNonNull(franchiseId, "franchiseId cannot be null");
        Objects.requireNonNull(name, "name cannot be null");

        Instant now = Instant.now();

        return new Branch(
                BranchId.generate(),
                franchiseId,
                name,
                now,
                now
        );

    }

    public static Branch reconstitute(BranchId id,FranchiseId franchiseId, Name name, Instant createdAt, Instant updatedAt) {
        Objects.requireNonNull(id, "branchId cannot be null");
        Objects.requireNonNull(franchiseId, "franchiseId cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");
        Objects.requireNonNull(updatedAt, "updatedAt cannot be null");

        return new Branch(id, franchiseId, name, createdAt, updatedAt);
    }

    public void updateName(Name newName) {
        Objects.requireNonNull(newName, "newName cannot be null");
        this.name = newName;
        this.updatedAt = Instant.now();
    }


    public BranchId getId() {
        return id;
    }

    public FranchiseId getFranchiseId() {
        return franchiseId;
    }

    public Name getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", franchiseId=" + franchiseId +
                ", name=" + name +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
