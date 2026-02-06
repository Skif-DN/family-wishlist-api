package com.skif.familywishlist.repositories;

import com.skif.familywishlist.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WishRepository extends JpaRepository<Wish, UUID> {
    List<Wish> findByOwnerId(UUID ownerId);
}
