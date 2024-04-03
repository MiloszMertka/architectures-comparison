package com.classic.elearning.user.adapter.ui;

public interface CreateUserFormUI {

    boolean isFormValid();

    void navigateToUserListView();

    void showErrorMessage(String message);

}
