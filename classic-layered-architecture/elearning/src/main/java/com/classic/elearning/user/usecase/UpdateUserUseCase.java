package com.classic.elearning.user.usecase;

import com.classic.elearning.user.usecase.model.UpdateUserModel;

public interface UpdateUserUseCase {

    void updateUser(String email, UpdateUserModel updateUserModel);

}
