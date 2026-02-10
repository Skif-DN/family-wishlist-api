package com.skif.familywishlist.controller;

import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.dto.person.PersonCreateDTO;
import com.skif.familywishlist.dto.person.PersonResponseDTO;
import com.skif.familywishlist.mapper.PersonMapper;
import com.skif.familywishlist.service.PersonService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "firstName",
            "lastName",
            "birthDate",
            "gender"
    );

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
    public ResponseEntity<List<PersonResponseDTO>> getAllPersonsByFamilyId(
                                                    @PathVariable UUID familyId,
                                                    @RequestParam(defaultValue = "firstName") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String direction
) throws BadRequestException {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException("Sorting by this field is not allowed");
        }

            Sort sort = Sort.by(
                    direction.equalsIgnoreCase("desc")
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC,
                    sortBy
            );

            return ResponseEntity.ok(
                    personService.getPersonsByFamilyId(familyId, sort)
                            .stream()
                            .map(PersonMapper::toDto)
                            .toList()
            );
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
