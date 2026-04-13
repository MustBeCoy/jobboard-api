package com.jobboardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;       // actual data
    private int pageNumber;        // current page
    private int pageSize;          // items per page
    private long totalElements;    // total records in DB
    private int totalPages;        // total pages
    private boolean lastPage;      // is this the last page?
}