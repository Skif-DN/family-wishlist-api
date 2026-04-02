package com.skif.familywishlist.dto.wish;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class WishResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private boolean fulfilled;
    private UUID ownerId;
    private OffsetDateTime createdAt;
    private OffsetDateTime fulfilledAt;

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getFulfilledAt() {
        return fulfilledAt;
    }

    public void setFulfilledAt(OffsetDateTime fulfilledAt) {
        this.fulfilledAt = fulfilledAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}
