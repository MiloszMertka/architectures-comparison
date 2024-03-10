package com.clean.elearning.user.adapter.ui.presenter;

import com.clean.elearning.user.adapter.dto.CreateUserRequest;
import com.clean.elearning.user.adapter.ui.CreateUserFormUI;
import com.clean.elearning.user.usecase.CreateUserUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateUserFormPresenter {

    private final CreateUserUseCase createUserUseCase;

    @Setter
    private CreateUserFormUI createUserFormUI;

    @Transactional
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
