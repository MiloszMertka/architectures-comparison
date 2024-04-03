package com.classic.elearning.user.service;

import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.infrastructure.dto.ChangePasswordRequest;
import com.classic.elearning.user.infrastructure.dto.CreateUserRequest;
import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationContext authenticationContext;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> browseUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> browseUsers(String searchText) {
        return userRepository.search(searchText);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(CreateUserRequest createUserRequest) {
        validateUserDoesNotExist(createUserRequest.getEmail());
        final var user = createUserFromRequest(createUserRequest);
        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(String email, UpdateUserRequest updateUserRequest) {
        validateEmailIsNotTaken(email, updateUserRequest.getEmail());
        final var user = getUserByEmail(email);
        updateUserFromRequest(user, updateUserRequest);
        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String email) {
        validateUserExists(email);
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        validateCurrentPassword(changePasswordRequest.getCurrentPassword(), user.getPassword());
        final var hashedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    private void validateCurrentPassword(String currentPassword, String hashedCurrentPassword) {
        if (!passwordEncoder.matches(currentPassword, hashedCurrentPassword)) {
            throw new IllegalStateException("Current password is incorrect");
        }
    }

    private void validateUserExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalStateException("User with email " + email + " does not exist");
        }
    }

    private void validateEmailIsNotTaken(String oldEmail, String newEmail) {
        if (!oldEmail.equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalStateException("User with email " + newEmail + " already exists");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    private void updateUserFromRequest(User user, UpdateUserRequest updateUserRequest) {
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setEmail(updateUserRequest.getEmail());
    }

    private void validateUserDoesNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("User with email " + email + " already exists");
        }
    }

    private User createUserFromRequest(CreateUserRequest createUserRequest) {
        final var hashedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        return new User(null, createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                hashedPassword,
                createUserRequest.getRole());
    }

}
