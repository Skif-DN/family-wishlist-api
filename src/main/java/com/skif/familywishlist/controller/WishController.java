package com.skif.familywishlist.controller;

import com.skif.familywishlist.domain.Wish;
import com.skif.familywishlist.dto.wish.WishDeleteRequestDTO;
import com.skif.familywishlist.dto.wish.WishRequestDTO;
import com.skif.familywishlist.dto.wish.WishResponseDTO;
import com.skif.familywishlist.mapper.WishMapper;
import com.skif.familywishlist.service.PersonService;
import com.skif.familywishlist.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService, PersonService personService) {
        this.wishService = wishService;
    }

    @GetMapping("/byOwner/{personId}")
    public List<WishResponseDTO> getWishesByOwner(@PathVariable UUID personId) {

        return wishService.getAllWishesByOwner(personId).stream()
                .map(WishMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<WishResponseDTO> createWish (@RequestBody WishRequestDTO dto) {

        Wish wish = wishService.addWish(
                dto.getOwnerId(),
                dto.getPin(),
                dto.getTitle(),
                dto.getDescription());

        return ResponseEntity.status(201).body(WishMapper.toDto(wish));
    }

    @PatchMapping("/{wishId}/fulfill")
    public ResponseEntity<WishResponseDTO> fulfillWish(
            @PathVariable UUID wishId,
            @RequestBody WishDeleteRequestDTO dto) {

        wishService.markAsFulfilled(wishId, dto.getPin());
        Wish updatedWish = wishService.getWishById(wishId);
        return ResponseEntity.ok(WishMapper.toDto(updatedWish));
    }

    @PatchMapping("/{wishId}/unfulfill")
    public ResponseEntity<WishResponseDTO> unfulfillWish(
            @PathVariable UUID wishId,
            @RequestBody WishDeleteRequestDTO dto) {

        wishService.markAsUnfulfilled(wishId, dto.getPin());
        Wish updatedWish = wishService.getWishById(wishId);
        return ResponseEntity.ok(WishMapper.toDto(updatedWish));
    }

    @PutMapping("/{wishId}")
    public ResponseEntity<WishResponseDTO> updateWish(@PathVariable UUID wishId,
                                                      @Valid @RequestBody WishRequestDTO dto) {
        Wish updatedWish = wishService.updateWish(wishId, dto);
        return ResponseEntity.ok(WishMapper.toDto(updatedWish));
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable UUID wishId,
            @RequestBody WishDeleteRequestDTO dto) {

        wishService.deleteWish(wishId, dto.getPin());
        return ResponseEntity.noContent().build();
    }
}
