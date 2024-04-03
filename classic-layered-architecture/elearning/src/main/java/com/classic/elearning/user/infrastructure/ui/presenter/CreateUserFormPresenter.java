package com.classic.elearning.user.infrastructure.ui.presenter;

import com.classic.elearning.user.infrastructure.dto.CreateUserRequest;
import com.classic.elearning.user.infrastructure.ui.CreateUserFormUI;
import com.classic.elearning.user.service.UserService;
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

    private final UserService userService;

    @Setter
    private CreateUserFormUI createUserFormUI;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSaveUserButtonClick(@NonNull CreateUserRequest createUserRequest) {
        if (!createUserFormUI.isFormValid()) {
            return;
        }

        try {
            userService.createUser(createUserRequest);
            createUserFormUI.navigateToUserListView();
        } catch (Exception exception) {
            createUserFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
