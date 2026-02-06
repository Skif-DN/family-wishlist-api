package com.skif.familywishlist.dto.family;

import java.util.List;
import java.util.UUID;

public class FamilyResponseDTO {
    private UUID familyId;
    private String name;
    private List<UUID> memberIds;

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

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<UUID> memberIds) {
        this.memberIds = memberIds;
    }
}
