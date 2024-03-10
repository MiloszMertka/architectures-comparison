package com.clean.elearning.user.usecase.interactor;

import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.domain.UserRepository;
import com.clean.elearning.user.usecase.CreateUserUseCase;
import com.clean.elearning.user.usecase.model.CreateUserModel;
import com.clean.elearning.user.usecase.port.PasswordHasher;

public class CreateUserInteractor implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUserInteractor(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void createUser(CreateUserModel createUserModel) {
        validateUserDoesNotExist(createUserModel.getEmail());
        final var user = createUserFromModel(createUserModel);
        userRepository.saveUser(user);
    }

    private void validateUserDoesNotExist(String email) {
        if (userRepository.userExistsByEmail(email)) {
            throw new IllegalStateException("User with email " + email + " already exists");
        }
    }

    private User createUserFromModel(CreateUserModel createUserModel) {
        final var hashedPassword = passwordHasher.hashPassword(createUserModel.getPassword());
        return new User(createUserModel.getFirstName(),
                createUserModel.getLastName(),
                createUserModel.getEmail(),
                hashedPassword,
                createUserModel.getRole());
    }

}
