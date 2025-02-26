package com.employees.demo.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public record ResponseDto(int totalElements, Collection<?> elements) implements Serializable {
    public ResponseDto(int totalElements, Collection<?> elements) {
        this.totalElements = totalElements;
        this.elements = new ArrayList<>(elements);
    }

    @Override
    public Collection<?> elements() {
        return new ArrayList<>(elements);
    }
}
