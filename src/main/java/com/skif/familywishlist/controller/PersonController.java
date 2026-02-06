package com.skif.familywishlist.controller;

import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.dto.person.PersonCreateDTO;
import com.skif.familywishlist.dto.person.PersonResponseDTO;
import com.skif.familywishlist.mapper.PersonMapper;
import com.skif.familywishlist.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonResponseDTO> getPersonById(@PathVariable UUID personId) {
        Person person = personService.getPersonById(personId);
        return person == null ? ResponseEntity.notFound().build()
                              : ResponseEntity.ok(PersonMapper.toDto(person));
    }

    @GetMapping("/family/{familyId}")
    public List<PersonResponseDTO> getAllPersonsByFamilyId(@PathVariable UUID familyId) {
        return personService.getPersonsByFamilyId(familyId).stream()
                .map(PersonMapper ::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@Valid @RequestBody PersonCreateDTO dto) {
            personService.createPerson(dto);
            return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deletePerson (@PathVariable UUID personId) {

        personService.deletePerson(personId);
            return ResponseEntity.noContent().build();
    }
}
