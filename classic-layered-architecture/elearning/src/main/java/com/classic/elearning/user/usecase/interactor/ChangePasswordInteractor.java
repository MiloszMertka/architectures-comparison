package com.classic.elearning.user.usecase.interactor;

import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.usecase.ChangePasswordUseCase;
import com.classic.elearning.user.usecase.model.ChangePasswordModel;
import com.classic.elearning.user.usecase.port.AuthContext;
import com.classic.elearning.user.usecase.port.PasswordHasher;

public class ChangePasswordInteractor implements ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuthContext authContext;

    public ChangePasswordInteractor(UserRepository userRepository, PasswordHasher passwordHasher, AuthContext authContext) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.authContext = authContext;
    }

    @Override
    public void changePassword(ChangePasswordModel changePasswordModel) {
        final var user = authContext.getCurrentUser();
        validateCurrentPassword(changePasswordModel.getCurrentPassword(), user.getPassword());
        final var hashedNewPassword = passwordHasher.hashPassword(changePasswordModel.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.saveUser(user);
    }

    private void validateCurrentPassword(String currentPassword, String hashedCurrentPassword) {
        if (!passwordHasher.verifyPasswordsMatch(currentPassword, hashedCurrentPassword)) {
            throw new IllegalStateException("Current password is incorrect");
        }
    }

}
