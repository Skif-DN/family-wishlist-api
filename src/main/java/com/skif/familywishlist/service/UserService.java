package com.skif.familywishlist.service;

import com.skif.familywishlist.domain.Family;
import com.skif.familywishlist.domain.Role;
import com.skif.familywishlist.domain.User;
import com.skif.familywishlist.repositories.FamilyRepository;
import com.skif.familywishlist.repositories.UserRepository;
import com.skif.familywishlist.utils.SecurityUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, FamilyRepository familyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    public User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return getUserByUsername(username);
    }

    @Transactional
    public void register (String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(username, passwordEncoder.encode(rawPassword));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Transactional
    public void changeUsername(String newUsername) {
        User user = getCurrentUser();

        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new SecurityException("Invalid password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteMe(String password) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("Invalid password");
        }

        Family family = user.getFamily();

        if (family != null) {
            familyRepository.delete(family);
        }

        userRepository.delete(user);
    }

    @Transactional
    public void removeUserByAdmin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User" + userId + "not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalStateException("Admin cannot delete another admin");
        }

        Family family = user.getFamily();

        if (family != null) {
            familyRepository.delete(family);
        }
        userRepository.delete(user);

    }

    public Family getMyFamily() {
        User user = getCurrentUser();

        if (user.getFamily() == null) {
            throw new IllegalStateException("User has no family");
        }

        return user.getFamily();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers(Sort sort) {
        return userRepository.findAll(sort);
    }
}
