package com.classic.elearning.user.infrastructure.ui;

public interface CreateUserFormUI {

    boolean isFormValid();

    void navigateToUserListView();

    void showErrorMessage(String message);

}
