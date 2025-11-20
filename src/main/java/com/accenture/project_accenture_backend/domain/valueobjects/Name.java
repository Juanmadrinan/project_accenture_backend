package com.accenture.project_accenture_backend.domain.valueobjects;

import com.accenture.project_accenture_backend.domain.exception.InvalidNameException;

import java.util.Objects;

/*
* Como regla de negocio que el nombre no puede ser menor de 3 caracter y no mayor a 100*/
public final class Name {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    private final String value;

    private Name(String value) {
        this.value = value;
    }

    public static Name of(String value) {
        validate(value);
        return new Name(value.trim());
    }


    private static void validate(String value) {
        if (value == null || value.isBlank()){
            throw new InvalidNameException("value name cannot be null or blank");

        }
        String trimmed = value.trim();
        if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
            throw new InvalidNameException("value name length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);
        }
    }


    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
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
