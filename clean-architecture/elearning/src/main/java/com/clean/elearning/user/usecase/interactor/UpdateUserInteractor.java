package com.clean.elearning.user.usecase.interactor;

import com.clean.elearning.user.domain.UserRepository;
import com.clean.elearning.user.usecase.UpdateUserUseCase;
import com.clean.elearning.user.usecase.model.UpdateUserModel;

public class UpdateUserInteractor implements UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void updateUser(UpdateUserModel updateUserModel) {

    }

}
