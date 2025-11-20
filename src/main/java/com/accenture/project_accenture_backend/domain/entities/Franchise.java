package com.accenture.project_accenture_backend.domain.entities;

import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.Stock;
import com.accenture.project_accenture_backend.domain.valueobjects.id.FranchiseId;

import java.time.Instant;
import java.util.Objects;

public class Franchise {
    private final FranchiseId id;
    private Name name;
    private final Instant createdAt;
    private Instant updatedAt;

    public Franchise(FranchiseId id, Instant createdAt, Instant updatedAt, Name name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
    }

    public static Franchise create(Name name) {
        Objects.requireNonNull(name, "Franchise name must not be null");

        Instant now = Instant.now();

        return new Franchise(
                FranchiseId.generate(),
                now,
                now,
                name
        );
    }

    public static Franchise reconstitute(FranchiseId id, Instant createdAt, Instant updatedAt, Name name) {
        Objects.requireNonNull(id, "Franchise id must not be null");
        Objects.requireNonNull(name, "Franchise name must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "Franchise updatedAt must not be null");

        return new Franchise(id, createdAt, updatedAt, name);
    }

     public void updateName(Name newName) {
        Objects.requireNonNull(newName, "Name must not be null");
        this.name = newName;
        this.updatedAt = Instant.now();
     }

    public FranchiseId getId() {
        return id;
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
        Franchise franchise = (Franchise) o;
        return Objects.equals(id, franchise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Franchise{" +
                "id=" + id +
                ", name=" + name +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
