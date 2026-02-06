package com.skif.familywishlist.mapper;

import com.skif.familywishlist.domain.User;
import com.skif.familywishlist.dto.user.UserResponseDTO;

public class UserMapper {

    public static UserResponseDTO toDto(User user) {
        if (user == null) return null;
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFamilyId(user.getFamily() != null ? user.getFamily().getId() : null);

        return dto;
    }
}
