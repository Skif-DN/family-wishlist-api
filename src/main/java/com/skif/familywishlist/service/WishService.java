package com.skif.familywishlist.service;

import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.domain.Wish;
import com.skif.familywishlist.dto.wish.WishRequestDTO;
import com.skif.familywishlist.repositories.PersonRepository;
import com.skif.familywishlist.repositories.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private  final PersonRepository personRepository;
    private final UserService userService;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;

    public WishService(WishRepository wishRepository, PersonRepository personRepository, UserService userService, PersonService personService, PasswordEncoder passwordEncoder) {
        this.wishRepository = wishRepository;
        this.personRepository = personRepository;
        this.userService = userService;
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Wish addWish(UUID ownerId, String pin, String title, String description) {
        Person owner = personRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        validateOwnerPin(owner, pin);

        Wish wish = new Wish(title, description, owner);
        owner.addWish(wish);

        wishRepository.save(wish);
        return wish;
    }

    @Transactional
    public void deleteWish(UUID wishId, String pin) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("Wish not found"));

        validateOwnerPin(wish.getOwner(), pin);

        wish.getOwner().removeWish(wish);
        wishRepository.delete(wish);
    }

    @Transactional
    public void markAsFulfilled(UUID wishId, String pin) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("Wish not found"));

        validateOwnerPin(wish.getOwner(), pin);

        if (wish.isFulfilled()) {
            throw new IllegalStateException("Wish already fulfilled");
        }
        wish.markAsFulfilled();
    }

    @Transactional
    public void markAsUnfulfilled(UUID wishId, String pin) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("Wish not found"));

        validateOwnerPin(wish.getOwner(), pin);

        if (!wish.isFulfilled()) {
            throw new IllegalStateException("Wish already unfulfilled");
        }
        wish.markAsUnfulfilled();
    }

    public Wish getWishById(UUID wishId) {
        return wishRepository.findById(wishId)
                .orElseThrow(()-> new IllegalArgumentException("Wish not found"));
    }

    public Page<Wish> getAllWishesByOwner(UUID ownerId, Pageable  pageable) {
        return wishRepository.findByOwnerId(ownerId, pageable);
    }

    @Transactional
    public Wish updateWish(UUID wishId, WishRequestDTO dto) {
        Person person = personService.getPersonById(dto.getOwnerId());

        if (!person.getUser().getId().equals(userService.getCurrentUser().getId())) {
            throw new SecurityException("Access denied");
        }

        Wish wish = person.getWishes().stream()
                .filter(w -> w.getId().equals(wishId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Wish not found"));

        wish.setTitle(dto.getTitle());
        wish.setDescription(dto.getDescription());


        return wishRepository.save(wish);
    }

    private void validateOwnerPin(Person owner, String pin) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner must not be null");
        }
        if (!owner.checkPin(pin, passwordEncoder)) {
            throw new SecurityException("Invalid PIN");
        }
    }
}
