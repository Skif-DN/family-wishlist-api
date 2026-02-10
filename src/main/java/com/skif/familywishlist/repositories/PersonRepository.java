package com.skif.familywishlist.repositories;

import com.skif.familywishlist.domain.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    @EntityGraph(attributePaths = "wishes")
    List<Person> findByFamilyId(UUID familyId, Sort sort);

    @EntityGraph(attributePaths = "wishes")
    Optional<Person> findById(UUID id);
}
