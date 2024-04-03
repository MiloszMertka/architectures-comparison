package com.classic.elearning.user.usecase.interactor;

import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.usecase.DeleteUserUseCase;

public class DeleteUserInteractor implements DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUser(String email) {
        validateUserExists(email);
        userRepository.deleteUserByEmail(email);
    }

    private void validateUserExists(String email) {
        if (!userRepository.userExistsByEmail(email)) {
            throw new IllegalStateException("User with email " + email + " does not exist");
        }
    }

}
