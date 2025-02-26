package com.employees.demo.dtos;

import java.util.Optional;

public class PaginationRequestDtoBuilder {

    private int page;
    private int pageSize;
    private String orderBy;
    private String orderByDir;
    private String searchLike;


    public PaginationRequestDtoBuilder setPage(int page) {
        this.page = page;
        return this;
    }

    public PaginationRequestDtoBuilder setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PaginationRequestDtoBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public PaginationRequestDtoBuilder setOrderByDir(String orderByDir) {
        this.orderByDir = orderByDir;
        return this;
    }

    public PaginationRequestDtoBuilder setSearchLike(String searchLike) {
        this.searchLike = searchLike;
        return this;
    }


    public PaginationRequestDto build(){
        return new PaginationRequestDto(page, pageSize, orderBy,
                orderByDir, Optional.ofNullable(searchLike));
    }
}
