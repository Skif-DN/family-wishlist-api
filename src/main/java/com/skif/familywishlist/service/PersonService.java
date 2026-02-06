package com.skif.familywishlist.service;

import com.skif.familywishlist.domain.Family;
import com.skif.familywishlist.domain.Gender;
import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.domain.User;
import com.skif.familywishlist.dto.person.PersonCreateDTO;
import com.skif.familywishlist.repositories.PersonRepository;
import com.skif.familywishlist.repositories.UserRepository;
import com.skif.familywishlist.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private  final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, UserService userService,  PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Person createPerson(PersonCreateDTO dto) {
        User user = userService.getCurrentUser();

        Family family = user.getFamily();

        if(family == null) {
            throw new IllegalStateException("Family has not been created yet");
        }

        LocalDate birthDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            birthDate = LocalDate.parse(dto.getBirthDate(), formatter);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid birth date format. Expected dd/MM/yyyy");
        }

        String pinHash = passwordEncoder.encode(dto.getPin());

        Person person = new Person(
                dto.getFirstName(),
                dto.getLastName(),
                birthDate,
                dto.getGender(),
                pinHash
        );

        person.setFamily(family);
        person.setUser(user);
        family.addMember(person);

        return personRepository.save(person);

    }

    public Person getPersonById(UUID personId) {
        return personRepository.findByIdWithWishes(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
    }

    public List<Person> getPersonsByFamilyId(UUID familyId) {
        return personRepository.findByFamilyIdWithWishes(familyId);
    }

    @Transactional
    public void deletePerson(UUID personId) {
        Person person = getPersonById(personId);

        User user = userService.getCurrentUser();
        Family family = user.getFamily();

        if (family == null || !family.getMembers().contains(person)) {
            throw new SecurityException("Access denied");
        }

        family.removeMember(person);
        person.setFamily(null);
        personRepository.delete(person);
    }
}
