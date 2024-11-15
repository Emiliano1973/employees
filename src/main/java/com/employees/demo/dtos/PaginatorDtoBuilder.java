package com.employees.demo.dtos;

import java.util.ArrayList;
import java.util.Collection;

public class PaginatorDtoBuilder {
    private int currentPage;
    private int currentPageTotalElements;
    private int totalPages;
    private int pageSize;
    private int totalElements;
    private Collection<?> elements;

    public PaginatorDtoBuilder setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PaginatorDtoBuilder setCurrentPageTotalElements(int currentPageTotalElements) {
        this.currentPageTotalElements = currentPageTotalElements;
        return this;
    }

    public PaginatorDtoBuilder setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public PaginatorDtoBuilder setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PaginatorDtoBuilder setTotalElements(int totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public PaginatorDtoBuilder setElements(Collection<?> elements) {
        this.elements = new ArrayList<>(elements);
        return this;
    }

    public PaginationDto createPaginatorDto() {
        return new PaginationDto(currentPage, currentPageTotalElements, totalPages, pageSize, totalElements, elements);
    }
}