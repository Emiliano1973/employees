package com.employees.demo.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public record PaginationDto(int currentPage, int currentPageTotalElements, int totalPages, int pageSize, int totalElements, Collection<?> elements)  implements  Serializable{
    public PaginationDto(int currentPage, int currentPageTotalElements, int totalPages, int pageSize, int totalElements, Collection<?> elements) {
        this.currentPage = currentPage;
        this.currentPageTotalElements=currentPageTotalElements;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.elements = new ArrayList<>(elements);
    }

    public Collection<?> elements() {
        return new ArrayList<>(elements);
    }

    @Override
    public String toString() {
        return "PaginationDto{" + "currentPage=" + currentPage +
                ", currentPageTotalElements=" + currentPageTotalElements +
                ", totalPages=" + totalPages +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                ", elements=[" +
                elements.stream().map(Object::toString).collect(Collectors.joining(", ")) +
                "]" +
                '}';
    }
}
