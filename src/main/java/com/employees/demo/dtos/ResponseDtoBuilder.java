package com.employees.demo.dtos;

import java.util.ArrayList;
import java.util.Collection;

public class ResponseDtoBuilder {

    private int totalElements;
    private Collection<?> elements;

    public ResponseDtoBuilder() {
        super();
    }

    public ResponseDtoBuilder setTotalElements(final int totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public ResponseDtoBuilder setElements(final Collection<?> elements) {
        this.elements = new ArrayList<>(elements);
        return this;
    }

    public ResponseDto createResponseDto() {
        return new ResponseDto(this.totalElements, this.elements);
    }

}
