package com.clean.elearning.user.usecase.interactor;

import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.domain.UserRepository;
import com.clean.elearning.user.usecase.UpdateUserUseCase;
import com.clean.elearning.user.usecase.model.UpdateUserModel;

public class UpdateUserInteractor implements UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void updateUser(String email, UpdateUserModel updateUserModel) {
        validateEmailIsNotTaken(email, updateUserModel.getEmail());
        final var user = getUserByEmail(email);
        updateUserFromModel(user, updateUserModel);
        userRepository.saveUser(user);
    }

    private void validateEmailIsNotTaken(String oldEmail, String newEmail) {
        if (!oldEmail.equals(newEmail) && userRepository.userExistsByEmail(newEmail)) {
            throw new IllegalStateException("User with email " + newEmail + " already exists");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }

    private void updateUserFromModel(User user, UpdateUserModel updateUserModel) {
        user.setFirstName(updateUserModel.getFirstName());
        user.setLastName(updateUserModel.getLastName());
        user.setEmail(updateUserModel.getEmail());
    }

}
