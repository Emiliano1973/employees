package com.employees.demo.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public record ResponseDto(int totalElements , Collection<? extends Serializable> elements) {
    public ResponseDto(int totalElements, Collection<? extends Serializable> elements) {
        this.totalElements = totalElements;
        this.elements = new ArrayList<>( elements);
    }

    @Override
    public Collection<? extends Serializable> elements() {
        return new ArrayList<>(elements);
    }
}
