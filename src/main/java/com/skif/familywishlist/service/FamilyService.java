package com.skif.familywishlist.service;

import com.skif.familywishlist.domain.Family;
import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.domain.User;
import com.skif.familywishlist.dto.person.PersonCreateDTO;
import com.skif.familywishlist.repositories.FamilyRepository;
import com.skif.familywishlist.repositories.PersonRepository;
import com.skif.familywishlist.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FamilyService {

    private FamilyRepository familyRepository;
    private UserRepository userRepository;
    private PersonRepository personRepository;
    private UserService userService;

    public FamilyService(FamilyRepository familyRepository, UserRepository userRepository, PersonRepository personRepository, UserService userService) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.userService = userService;
    }

    @Transactional
    public Family createFamily(String name) {

        User user = userService.getCurrentUser();

        if (user.getFamily() != null) {
            throw new IllegalStateException("User already has a family");
        }

        Family family = new Family(name);
        familyRepository.save(family);

        user.setFamily(family);
        userRepository.save(user);

        return family;
    }

    public List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }

    public Family getFamily(UUID id) {
        return familyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Family" + id + "not found"));
    }

    public Family getFamilyByUser() {
        User user = userService.getCurrentUser();
        Family family = user.getFamily();

        if (family == null) {
            throw new IllegalStateException("User has no family");
        }
        return family;
    }

    @Transactional
    public void addMemberToFamily(PersonCreateDTO dto) {
        User user = userService.getCurrentUser();
        Family family = user.getFamily();

        if (family == null) {
            throw new IllegalStateException("User has no family");
        }

        LocalDate birthDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            birthDate = LocalDate.parse(dto.getBirthDate(), formatter);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid birth date format. Expected dd/MM/yyyy");
        }

        Person person = new Person(
                dto.getFirstName(),
                dto.getLastName(),
                birthDate,
                dto.getGender(),
                dto.getPin()
        );

        person.setFamily(family);
        family.addMember(person);
        personRepository.save(person);
    }

    @Transactional
    public void removeMemberFromFamily(UUID personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person" + personId + "not found"));

        User currentUser = userService.getCurrentUser();

        Family family = currentUser.getFamily();

        if (family == null || !family.equals(person.getFamily())) {
            throw new SecurityException("You do not own this family");
        }

        family.removeMember(person);
        person.setFamily(null);
        personRepository.save(person);

    }

    public List<Person> getMembers(UUID familyId) {
        User currentUser = userService.getCurrentUser();
        Family family = currentUser.getFamily();

        if (family == null) {
            throw new IllegalStateException("User has no family");
        }

        return new ArrayList<>(family.getMembers());
    }

    @Transactional
    public void deleteFamily() {
        User user = userService.getCurrentUser();

        Family family = user.getFamily();

        if (family == null) {
            throw new IllegalStateException("User has no family");
        }

        user.setFamily(null);
        familyRepository.delete(family);
    }
}



