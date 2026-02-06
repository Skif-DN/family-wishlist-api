package com.skif.familywishlist.mapper;

import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.dto.person.PersonResponseDTO;

public class PersonMapper {

    public static PersonResponseDTO toDto(Person person) {
        if (person == null) return null;

        PersonResponseDTO dto = new PersonResponseDTO();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setBirthDate(person.getBirthDate() != null ? person.getBirthDate().toString() : null);
        dto.setGender(person.getGender());
        dto.setWishes(person.getWishes().stream()
                .map(w -> w.getTitle())
                .toList());
        dto.setUsername(person.getUser() != null ? person.getUser().getUsername() : null);

        return dto;
    }
}