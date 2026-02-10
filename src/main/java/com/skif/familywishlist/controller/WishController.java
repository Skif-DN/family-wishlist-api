package com.skif.familywishlist.controller;

import com.skif.familywishlist.domain.Wish;
import com.skif.familywishlist.dto.wish.WishDeleteRequestDTO;
import com.skif.familywishlist.dto.wish.WishRequestDTO;
import com.skif.familywishlist.dto.wish.WishResponseDTO;
import com.skif.familywishlist.mapper.WishMapper;
import com.skif.familywishlist.service.PersonService;
import com.skif.familywishlist.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/wishes")
public class WishController {
    private final WishService wishService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "title",
            "description",
            "createdAt",
            "fulfilledAt"
    );

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/byOwner/{personId}")
    public ResponseEntity<Page<WishResponseDTO>> getWishesByOwner(@PathVariable UUID personId,
                                                                  @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC)
                                                                  Pageable pageable) {

        if (pageable.getPageSize() > 50) {
            pageable = PageRequest.of(pageable.getPageNumber(), 50, pageable.getSort());
        }

        pageable = validatePageable(pageable);

        Page<WishResponseDTO> dtoPage =
                wishService.getAllWishesByOwner(personId, pageable)
                        .map(WishMapper::toDto);

        return ResponseEntity.ok(dtoPage);
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

    private Pageable validatePageable(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException(
                        "Sorting by '" + order.getProperty() + "' is not allowed"
                );
            }
        }
        return pageable;
    }
}
