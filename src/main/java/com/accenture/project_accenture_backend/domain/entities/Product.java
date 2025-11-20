package com.accenture.project_accenture_backend.domain.entities;

import com.accenture.project_accenture_backend.domain.valueobjects.Name;
import com.accenture.project_accenture_backend.domain.valueobjects.Stock;
import com.accenture.project_accenture_backend.domain.valueobjects.id.BranchId;
import com.accenture.project_accenture_backend.domain.valueobjects.id.ProductId;

import java.time.Instant;
import java.util.Objects;

public class Product {
    private final ProductId id;
    private final BranchId branchId;
    private Name name;
    private Stock stock;
    private final Instant createdAt;
    private Instant updatedAt;

    public Product(ProductId id, BranchId branchId, Instant createdAt, Name name, Stock stock, Instant updatedAt) {
        this.id = id;
        this.branchId = branchId;
        this.createdAt = createdAt;
        this.name = name;
        this.stock = stock;
        this.updatedAt = updatedAt;
    }

    public static Product create(BranchId branchId, Name name, Stock initialStock) {
        Objects.requireNonNull(branchId, "Branch Id cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(initialStock, "Initial Stock cannot be null");

        Instant now = Instant.now();
        return new Product(
                ProductId.generate(),
                branchId,
                now,
                name,
                initialStock,
                now
        );
    }

    /*Esta linea me permite reconstituir un producto si viene ya en la base de datos, es un opcional*/
    public static Product reconstitute(ProductId id, BranchId branchId, Instant createdAt, Name name, Stock initialStock, Instant updatedAt) {
        Objects.requireNonNull(id, "Product Id cannot be null");
        Objects.requireNonNull(branchId, "Branch Id cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(initialStock, "Initial Stock cannot be null");
        Objects.requireNonNull(createdAt, "Created At cannot be null");
        Objects.requireNonNull(updatedAt, "Updated At cannot be null");

        return new Product(id, branchId, createdAt, name, initialStock, updatedAt);
    }

    public void updateName(Name newName) {
        Objects.requireNonNull(newName, "Name cannot be null");
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    public void updateStock(Stock newStock) {
        Objects.requireNonNull(newStock, "Stock cannot be null");
        this.stock = newStock;
        this.updatedAt = Instant.now();
    }

    public void addStock(int quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        this.stock = this.stock.add(quantity);
        this.updatedAt = Instant.now();
    }

    public void removeStock(int quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        this.stock = this.stock.subtract(quantity);
        this.updatedAt = Instant.now();
    }

    public boolean hasStock() {
        return this.stock.hasStock();
    }

    public ProductId getId() {
        return id;
    }

    public BranchId getBranchId() {
        return branchId;
    }

    public Name getName() {
        return name;
    }

    public Stock getStock() {
        return stock;
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
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", branchId=" + branchId +
                ", name=" + name +
                ", stock=" + stock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
