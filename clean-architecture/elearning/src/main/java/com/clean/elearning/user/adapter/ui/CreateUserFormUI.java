package com.clean.elearning.user.adapter.ui;

public interface CreateUserFormUI {

    boolean isFormValid();

    void navigateToUserListView();

    void showErrorMessage(String message);

}
