package com.skif.familywishlist.dto.wish;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class WishRequestDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;
    private UUID ownerId;
    private String pin;

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

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
