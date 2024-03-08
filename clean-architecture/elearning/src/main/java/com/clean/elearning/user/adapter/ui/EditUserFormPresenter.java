package com.clean.elearning.user.adapter.ui;

import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.infrastructure.view.EditUserFormView;
import com.clean.elearning.user.usecase.UpdateUserUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EditUserFormPresenter {

    private final UpdateUserUseCase updateUserUseCase;

    @Setter
    private EditUserFormView editUserFormView;

    @Transactional
    public void handleSaveUserButtonClick(@NonNull String userEmail, @NonNull UpdateUserRequest updateUserRequest) {
        if (!editUserFormView.isFormValid()) {
            return;
        }

        try {
            updateUserUseCase.updateUser(userEmail, updateUserRequest);
            editUserFormView.navigateToUserListView();
        } catch (Exception exception) {
            editUserFormView.showErrorMessage(exception.getMessage());
        }
    }

}
