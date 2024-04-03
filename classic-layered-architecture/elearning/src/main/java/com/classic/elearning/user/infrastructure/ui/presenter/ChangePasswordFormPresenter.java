package com.classic.elearning.user.infrastructure.ui.presenter;

import com.classic.elearning.user.infrastructure.dto.ChangePasswordRequest;
import com.classic.elearning.user.infrastructure.ui.ChangePasswordFormUI;
import com.classic.elearning.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChangePasswordFormPresenter {

    private final UserService userService;

    @Setter
    private ChangePasswordFormUI changePasswordFormUI;

    @Transactional
    public void handleChangePasswordButtonClick(@NonNull ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordFormUI.isFormValid()) {
            return;
        }

        try {
            userService.changePassword(changePasswordRequest);
            changePasswordFormUI.navigateToHomeView();
        } catch (Exception exception) {
            changePasswordFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
