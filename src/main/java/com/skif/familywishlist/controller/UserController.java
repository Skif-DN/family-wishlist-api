package com.skif.familywishlist.controller;

import com.skif.familywishlist.dto.family.FamilyResponseDTO;
import com.skif.familywishlist.dto.user.*;
import com.skif.familywishlist.mapper.FamilyMapper;
import com.skif.familywishlist.mapper.UserMapper;
import com.skif.familywishlist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponseDTO getMe() {
        return UserMapper.toDto(userService.getCurrentUser());
    }

    @PatchMapping("/me/username")
    public ResponseEntity<Void> changeUsername(@Valid @RequestBody ChangeUsernameDTO dto) {
        userService.changeUsername(dto.getNewUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto.getOldPassword(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/family")
    public FamilyResponseDTO getMyFamily() {
        return FamilyMapper.toDto(userService.getMyFamily());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@Valid@RequestBody UserDeleteDTO dto) {
        userService.deleteMe(dto.getPassword());
        return ResponseEntity.noContent().build();
    }
}
