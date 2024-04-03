package com.classic.elearning.user.infrastructure.ui;

public interface ChangePasswordFormUI {

    boolean isFormValid();

    void navigateToHomeView();

    void showErrorMessage(String message);

}
