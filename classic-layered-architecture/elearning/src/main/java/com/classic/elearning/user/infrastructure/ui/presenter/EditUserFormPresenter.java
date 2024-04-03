package com.classic.elearning.user.infrastructure.ui.presenter;

import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.infrastructure.ui.EditUserFormUI;
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
public class EditUserFormPresenter {

    private final UserService userService;

    @Setter
    private EditUserFormUI editUserFormUI;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSaveUserButtonClick(@NonNull String userEmail, @NonNull UpdateUserRequest updateUserRequest) {
        if (!editUserFormUI.isFormValid()) {
            return;
        }

        try {
            userService.updateUser(userEmail, updateUserRequest);
            editUserFormUI.navigateToUserListView();
        } catch (Exception exception) {
            editUserFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
