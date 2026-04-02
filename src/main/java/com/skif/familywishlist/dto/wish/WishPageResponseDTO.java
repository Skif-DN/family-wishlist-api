package com.skif.familywishlist.dto.wish;

import java.util.List;

public class WishPageResponseDTO {
    private List<WishResponseDTO> context;
    private int totalPages;
    private long totalElements;
    private long fulfilledCount;

    public WishPageResponseDTO(List<WishResponseDTO> context, int totalPages, long totalElements, long fulfilledCount) {
        this.context = context;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.fulfilledCount = fulfilledCount;
    }

    public List<WishResponseDTO> getContext() {
        return context;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getFulfilledCount() {
        return fulfilledCount;
    }
}
