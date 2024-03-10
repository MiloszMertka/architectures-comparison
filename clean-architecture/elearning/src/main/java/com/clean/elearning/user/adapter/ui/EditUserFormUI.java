package com.clean.elearning.user.adapter.ui;

import com.clean.elearning.user.adapter.dto.UpdateUserRequest;

public interface EditUserFormUI {

    void setUser(UpdateUserRequest user);

    boolean isFormValid();

    void navigateToUserListView();

    void showErrorMessage(String message);

}
