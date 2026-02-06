package com.skif.familywishlist.mapper;

import com.skif.familywishlist.domain.Wish;
import com.skif.familywishlist.dto.wish.WishResponseDTO;

public class WishMapper {
    public static WishResponseDTO toDto(Wish wish) {
        if (wish == null) return null;

        WishResponseDTO dto = new WishResponseDTO();
                dto.setId(wish.getId());
                dto.setTitle(wish.getTitle());
                dto.setDescription(wish.getDescription());
                dto.setFulfilled(wish.isFulfilled());
                dto.setOwnerId(wish.getOwner().getId());
        return dto;
    }
}
