package com.clean.elearning.user.adapter.ui.presenter;

import com.clean.elearning.user.adapter.dto.ChangePasswordRequest;
import com.clean.elearning.user.adapter.ui.ChangePasswordFormUI;
import com.clean.elearning.user.usecase.ChangePasswordUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChangePasswordFormPresenter {

    private final ChangePasswordUseCase changePasswordUseCase;

    @Setter
    private ChangePasswordFormUI changePasswordFormUI;

    @Transactional
    public void handleChangePasswordButtonClick(@NonNull ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordFormUI.isFormValid()) {
            return;
        }

        try {
            changePasswordUseCase.changePassword(changePasswordRequest);
            changePasswordFormUI.navigateToHomeView();
        } catch (Exception exception) {
            changePasswordFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
