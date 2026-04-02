package com.skif.familywishlist.service;

import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.domain.User;
import com.skif.familywishlist.domain.Wish;
import com.skif.familywishlist.dto.wish.WishRequestDTO;
import com.skif.familywishlist.repositories.PersonRepository;
import com.skif.familywishlist.repositories.WishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class WishServiceTest {
    @Mock
    private WishRepository wishRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserService userService;

    @Mock
    private PersonService personService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private WishService wishService;

    @Test
    void addWish_shouldCreateWish_whenPinIsValid() {

        UUID ownerId = UUID.randomUUID();
        String pin = "1234";

        Person person = Mockito.mock(Person.class);

        when(personRepository.findById(ownerId))
                .thenReturn(Optional.of(person));

        when(person.checkPin(pin, passwordEncoder))
                .thenReturn(true);

        Wish result = wishService.addWish(ownerId, pin, "Bike", "Mountain bike");

        assertEquals("Bike", result.getTitle());
        assertEquals("Mountain bike", result.getDescription());

        verify(wishRepository).save(any(Wish.class));
    }

    @Test
    void addWish_shouldThrowException_whenPersonNotFound() {

        UUID ownerId = UUID.randomUUID();
        String pin = "1234";

        when(personRepository.findById(ownerId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> wishService.addWish(ownerId, pin, "Bike", "Mountain bike")
        );

        assertEquals("Person not found", exception.getMessage());

        verify(wishRepository, never()).save(any());
    }

    @Test
    void addWish_shouldThrowSecurityException_whenPinInvalid(){
        UUID ownerId = UUID.randomUUID();
        String pin = "1234";

        Person person = Mockito.mock(Person.class);

        when(personRepository.findById(ownerId))
        .thenReturn(Optional.of(person));

        when(person.checkPin(pin, passwordEncoder))
                .thenReturn(false);

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> wishService.addWish(ownerId, pin, "Bike", "Mountain bike"));

            assertEquals("Invalid PIN", exception.getMessage());

            verify(person).checkPin(pin, passwordEncoder);
            verifyNoInteractions(wishRepository);
            verify(wishRepository, never()).save(any());
    }

    @Test
    void markAsFulfilled_shouldThrowException_whenAlreadyFulfilled() {
        UUID wishId = UUID.randomUUID();
        String pin = "1234";

        Wish wish = Mockito.mock(Wish.class);
        Person person = Mockito.mock(Person.class);

        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));

        when(wish.getOwner()).thenReturn(person);

        when(person.checkPin(pin, passwordEncoder))
        .thenReturn(true);

        when(wish.isFulfilled()).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> wishService.markAsFulfilled(wishId, pin));

        assertEquals("Wish already fulfilled", exception.getMessage());

        verify(wish, never()).markAsFulfilled();
    }

    @Test
    void markAsFulfilled_shouldThrowException_whenAlreadyUnfulfilled() {
        UUID wishId = UUID.randomUUID();
        String pin = "1234";

        Wish wish = Mockito.mock(Wish.class);
        Person person = Mockito.mock(Person.class);

        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));

        when(wish.getOwner()).thenReturn(person);

        when(person.checkPin(pin, passwordEncoder))
                .thenReturn(true);

        when(wish.isFulfilled()).thenReturn(false);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> wishService.markAsUnfulfilled(wishId, pin));

        assertEquals("Wish already unfulfilled", exception.getMessage());

        verify(wish, never()).markAsUnfulfilled();
    }

    @Test
    void updateWish_shouldThrowSecurityException_whenNotOwner(){
        UUID ownerId = UUID.randomUUID();
        UUID wishId = UUID.randomUUID();

        WishRequestDTO wishRequestDTO = new WishRequestDTO();
        wishRequestDTO.setOwnerId(ownerId);
        wishRequestDTO.setTitle("New title");
        wishRequestDTO.setDescription("New description");

        Person person = Mockito.mock(Person.class);
        User ownerUser = Mockito.mock(User.class);
        User currentUser = Mockito.mock(User.class);

        when(personService.getPersonById(ownerId))
                .thenReturn(person);

        when(person.getUser()).thenReturn(ownerUser);
        when(ownerUser.getId()).thenReturn(UUID.randomUUID());

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.getId()).thenReturn(UUID.randomUUID());

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> wishService.updateWish(wishId,  wishRequestDTO));

        assertEquals("Access denied", exception.getMessage());

        verify(wishRepository, never()).save(any());
    }

    @Test
    void updateWish_shouldUpdateWish_whenOwnerMatches(){
        UUID ownerId = UUID.randomUUID();
        UUID wishId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        WishRequestDTO wishRequestDTO = new WishRequestDTO();
        wishRequestDTO.setOwnerId(ownerId);
        wishRequestDTO.setTitle("New title");
        wishRequestDTO.setDescription("New description");
        wishRequestDTO.setPin("1234");

        Person person = Mockito.mock(Person.class);
        User ownerUser = Mockito.mock(User.class);
        User currentUser = Mockito.mock(User.class);

        Wish wish = new Wish("Old title", "Old description", person);
        wish.setId(wishId);

        when(person.checkPin("1234", passwordEncoder)).thenReturn(true);

        when(personService.getPersonById(ownerId))
                .thenReturn(person);

        when(person.getUser()).thenReturn(ownerUser);
        when(ownerUser.getId()).thenReturn(userId);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.getId()).thenReturn(userId);

        when(person.getWishes()).thenReturn(List.of(wish));
        when(wishRepository.save(wish)).thenReturn(wish);

        Wish updatedWish = wishService.updateWish(wishId, wishRequestDTO);

        assertEquals("New title", updatedWish.getTitle());
        assertEquals("New description", updatedWish.getDescription());

        verify(wishRepository).save(wish);
    }

    @Test
    void deleteWish_shouldDeleteWish(){
        UUID wishId = UUID.randomUUID();

        String pin = "1234";

        Wish wish = Mockito.mock(Wish.class);
        Person person = Mockito.mock(Person.class);

        when(wishRepository.findById(wishId)).thenReturn(Optional.of(wish));
        when(wish.getOwner()).thenReturn(person);
        when(person.checkPin(pin, passwordEncoder)).thenReturn(true);

        wishService.deleteWish(wishId, pin);

        verify(person).removeWish(wish);
        verify(wishRepository).delete(wish);
    }

    @Test
    void deleteWish_shouldThrowException_whenWishNotFound(){
        UUID wishId = UUID.randomUUID();
        String pin = "1234";

        when(wishRepository.findById(wishId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> wishService.deleteWish(wishId, pin)
        );
        assertEquals("Wish not found", exception.getMessage());

        verify(wishRepository,  never()).delete(any());
    }

    @Test
    void deleteWish_shouldThrowSecurityException_whenPinInvalid(){
        UUID wishId = UUID.randomUUID();
        String pin = "1234";

        Wish wish = Mockito.mock(Wish.class);
        Person person = Mockito.mock(Person.class);

        when(wishRepository.findById(wishId)).thenReturn(Optional.of(wish));
        when(wish.getOwner()).thenReturn(person);
        when(person.checkPin(pin, passwordEncoder)).thenReturn(false);

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> wishService.deleteWish(wishId, pin)
        );

        assertEquals("Invalid PIN", exception.getMessage());

        verify(wish).getOwner();
        verify(person).checkPin(pin, passwordEncoder);
        verify(wishRepository, never()).delete(any());
    }
}
