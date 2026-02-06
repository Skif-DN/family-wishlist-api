package com.skif.familywishlist.controller;

import com.skif.familywishlist.domain.Family;
import com.skif.familywishlist.dto.family.FamilyRequestDTO;
import com.skif.familywishlist.dto.family.FamilyResponseDTO;
import com.skif.familywishlist.mapper.FamilyMapper;
import com.skif.familywishlist.service.FamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/families")
public class FamilyController {
    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping
    public ResponseEntity<FamilyResponseDTO> createFamily(@RequestBody FamilyRequestDTO dto) {
        Family family = familyService.createFamily(dto.getName());
        return ResponseEntity.status(201).body(FamilyMapper.toDto(family));
    }

    @GetMapping("/me")
    public ResponseEntity<FamilyResponseDTO> getMyFamily() {
        Family family = familyService.getFamilyByUser();

        return ResponseEntity.ok(FamilyMapper.toDto(family));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteFamily() {
        familyService.deleteFamily();
        return ResponseEntity.noContent().build();
    }
}
