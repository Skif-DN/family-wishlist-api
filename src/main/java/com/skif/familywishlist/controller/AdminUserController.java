package com.skif.familywishlist.controller;

import com.skif.familywishlist.dto.user.UserResponseDTO;
import com.skif.familywishlist.mapper.UserMapper;
import com.skif.familywishlist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getUsers()
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
