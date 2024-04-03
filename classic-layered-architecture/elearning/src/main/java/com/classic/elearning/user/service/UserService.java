package com.classic.elearning.user.service;

import com.classic.elearning.shared.service.SecurityService;
import com.classic.elearning.user.infrastructure.dto.ChangePasswordRequest;
import com.classic.elearning.user.infrastructure.dto.CreateUserRequest;
import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public List<User> browseUsers() {
        return userRepository.getAllUsers();
    }

    public List<User> browseUsers(String searchText) {
        return userRepository.searchUsers(searchText);
    }

    public void createUser(CreateUserRequest createUserRequest) {
        validateUserDoesNotExist(createUserRequest.getEmail());
        final var user = createUserFromRequest(createUserRequest);
        userRepository.saveUser(user);
    }

    public void updateUser(String email, UpdateUserRequest updateUserRequest) {
        validateEmailIsNotTaken(email, updateUserRequest.getEmail());
        final var user = getUserByEmail(email);
        updateUserFromRequest(user, updateUserRequest);
        userRepository.saveUser(user);
    }

    public void deleteUser(String email) {
        validateUserExists(email);
        userRepository.deleteUserByEmail(email);
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        final var user = securityService.getCurrentUser();
        validateCurrentPassword(changePasswordRequest.getCurrentPassword(), user.getPassword());
        final var hashedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.saveUser(user);
    }

    private void validateCurrentPassword(String currentPassword, String hashedCurrentPassword) {
        if (!passwordEncoder.matches(currentPassword, hashedCurrentPassword)) {
            throw new IllegalStateException("Current password is incorrect");
        }
    }

    private void validateUserExists(String email) {
        if (!userRepository.userExistsByEmail(email)) {
            throw new IllegalStateException("User with email " + email + " does not exist");
        }
    }

    private void validateEmailIsNotTaken(String oldEmail, String newEmail) {
        if (!oldEmail.equals(newEmail) && userRepository.userExistsByEmail(newEmail)) {
            throw new IllegalStateException("User with email " + newEmail + " already exists");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }

    private void updateUserFromRequest(User user, UpdateUserRequest updateUserRequest) {
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setEmail(updateUserRequest.getEmail());
    }

    private void validateUserDoesNotExist(String email) {
        if (userRepository.userExistsByEmail(email)) {
            throw new IllegalStateException("User with email " + email + " already exists");
        }
    }

    private User createUserFromRequest(CreateUserRequest createUserRequest) {
        final var hashedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        return new User(createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                createUserRequest.getEmail(),
                hashedPassword,
                createUserRequest.getRole());
    }

}
