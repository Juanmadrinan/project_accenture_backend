package com.accenture.project_accenture_backend.domain.valueobjects;

import com.accenture.project_accenture_backend.domain.exception.InvalidStockException;

import java.util.Objects;

/*
* - El Stock no puede tener cantidades negativas
* - La Suma y Resta retorna nuevos objetos (inmutabilidad)
* */
public final class Stock {
    private final int value;

    private Stock(int value) {
        this.value = value;
    }

    public static Stock of(int value) {
        if (value < 0) {
            throw new InvalidStockException("You cannot add a negative amount.");
        }
        return new Stock(value);
    }

    public static Stock zero() {
        return new Stock(0);
    }

    public Stock add(int quantity) {
        if (quantity < 0) {
            throw new InvalidStockException("You cannot add a negative amount.");
        }
        return new Stock(this.value + quantity);
    }

    public Stock subtract(int quantity) {
        if (quantity < 0) {
            throw new InvalidStockException("You cannot subtract a negative amount.");
        }
        if (this.value < quantity) {
            throw new InvalidStockException("You cannot subtract a negative amount.");
        }

        return new Stock(this.value - quantity);
    }

    public boolean hasStock() {
        return value > 0;
    }

    /*Recordatorio: Compara si este Stock es mayor que otro*/
    public boolean isGreaterThan(Stock other) {
        return this.value > other.value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return value == stock.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
