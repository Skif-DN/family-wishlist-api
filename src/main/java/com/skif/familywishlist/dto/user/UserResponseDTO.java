package com.skif.familywishlist.dto.user;

import java.util.UUID;

public class UserResponseDTO {
    public UUID id;
    public String username;
    public UUID familyId;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }
}
