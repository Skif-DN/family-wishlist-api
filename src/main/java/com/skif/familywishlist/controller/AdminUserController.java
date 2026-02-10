package com.skif.familywishlist.controller;

import com.skif.familywishlist.dto.user.UserResponseDTO;
import com.skif.familywishlist.mapper.UserMapper;
import com.skif.familywishlist.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "username",
            "role"
    );

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) throws BadRequestException {

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException("Sorting by this field is not allowed");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return userService.getUsers(sort)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.removeUserByAdmin(userId);
        return ResponseEntity.noContent().build();
    }
}
