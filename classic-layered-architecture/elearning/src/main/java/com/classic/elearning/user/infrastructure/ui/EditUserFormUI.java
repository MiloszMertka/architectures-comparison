package com.classic.elearning.user.infrastructure.ui;

import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;

public interface EditUserFormUI {

    void setUser(UpdateUserRequest user);

    boolean isFormValid();

    void navigateToUserListView();

    void showErrorMessage(String message);

}
