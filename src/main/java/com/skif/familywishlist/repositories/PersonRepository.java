package com.skif.familywishlist.repositories;

import com.skif.familywishlist.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    List<Person> findByFamilyId(UUID familyId);

    @Query("SELECT p FROM Person p LEFT JOIN FETCH p.wishes WHERE p.id = :id")
    Optional<Person> findByIdWithWishes(@Param("id") UUID id);

    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.wishes WHERE p.family.id = :familyId")
    List<Person> findByFamilyIdWithWishes(@Param("familyId") UUID familyId);
}
