package com.clean.elearning.user.adapter.ui.presenter;

import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.adapter.ui.EditUserFormUI;
import com.clean.elearning.user.usecase.UpdateUserUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EditUserFormPresenter {

    private final UpdateUserUseCase updateUserUseCase;

    @Setter
    private EditUserFormUI editUserFormUI;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSaveUserButtonClick(@NonNull String userEmail, @NonNull UpdateUserRequest updateUserRequest) {
        if (!editUserFormUI.isFormValid()) {
            return;
        }

        try {
            updateUserUseCase.updateUser(userEmail, updateUserRequest);
            editUserFormUI.navigateToUserListView();
        } catch (Exception exception) {
            editUserFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
