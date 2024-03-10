package com.clean.elearning.user.adapter.ui;

public interface ChangePasswordFormUI {

    boolean isFormValid();

    void navigateToHomeView();

    void showErrorMessage(String message);

}
