package com.employees.demo.dtos;

import java.io.Serializable;
import java.util.Optional;

public record PaginationRequestDto(int page, int pageSize, String orderBy,
                                   String orderByDir, Optional<String> searchLike) implements Serializable {
}
