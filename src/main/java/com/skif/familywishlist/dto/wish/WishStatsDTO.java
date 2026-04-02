package com.skif.familywishlist.dto.wish;

public class WishStatsDTO {
    private long totalWishes;
    private long fulfilledWishes;

    public WishStatsDTO(long totalWishes, long fulfilledWishes) {
        this.totalWishes = totalWishes;
        this.fulfilledWishes = fulfilledWishes;
    }

    public long getTotalWishes() { return totalWishes; }
    public long getFulfilledWishes() { return fulfilledWishes; }
}
