package com.clean.elearning.user.usecase;

import com.clean.elearning.user.usecase.model.UpdateUserModel;

public interface UpdateUserUseCase {

    void updateUser(String email, UpdateUserModel updateUserModel);

}
