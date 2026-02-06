package com.skif.familywishlist.repositories;

import com.skif.familywishlist.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FamilyRepository extends JpaRepository<Family, UUID> {
    Optional<Family> findByName(String name);

    boolean existsByName(String name);
}
