package com.clean.elearning.user.adapter.ui;

import com.clean.elearning.user.adapter.dto.CreateUserRequest;

public interface CreateUserFormUI {

    void setUser(CreateUserRequest user);

    boolean isFormValid();

    void navigateToUserListView();

    void showErrorMessage(String message);

}
