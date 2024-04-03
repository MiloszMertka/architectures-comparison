package com.classic.elearning.user.adapter.ui.presenter;

import com.classic.elearning.user.adapter.dto.CreateUserRequest;
import com.classic.elearning.user.adapter.ui.CreateUserFormUI;
import com.classic.elearning.user.usecase.CreateUserUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateUserFormPresenter {

    private final CreateUserUseCase createUserUseCase;

    @Setter
    private CreateUserFormUI createUserFormUI;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSaveUserButtonClick(@NonNull CreateUserRequest createUserRequest) {
        if (!createUserFormUI.isFormValid()) {
            return;
        }

        try {
            createUserUseCase.createUser(createUserRequest);
            createUserFormUI.navigateToUserListView();
        } catch (Exception exception) {
            createUserFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
