package com.skif.familywishlist.dto.wish;

import java.util.UUID;

public class WishDeleteRequestDTO {
    private UUID id;
    private String pin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
