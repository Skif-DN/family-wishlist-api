package com.skif.familywishlist.mapper;

import com.skif.familywishlist.domain.Family;
import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.dto.family.FamilyResponseDTO;

import java.util.List;

public class FamilyMapper {
    public static FamilyResponseDTO toDto(Family family) {
        if (family == null) return null;

        FamilyResponseDTO dto = new FamilyResponseDTO();
        dto.setFamilyId(family.getId());
        dto.setName(family.getName() != null ? family.getName() : "");
        dto.setMemberIds(family.getMembers() != null
                ? family.getMembers().stream().map(Person::getId).toList()
                : List.of());
        return dto;
    }
}
