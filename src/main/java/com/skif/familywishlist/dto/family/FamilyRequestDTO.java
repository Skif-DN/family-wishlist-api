package com.skif.familywishlist.dto.family;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class FamilyRequestDTO {
    private UUID familyId;
    private UUID personId;

    @NotBlank(message = "Family name is required")
    private String name;

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
