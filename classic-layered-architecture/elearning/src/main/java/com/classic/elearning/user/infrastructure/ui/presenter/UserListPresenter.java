package com.classic.elearning.user.infrastructure.ui.presenter;

import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.infrastructure.ui.UserListUI;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;
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
public class UserListPresenter {

    private final UserService userService;

    @Setter
    private UserListUI userListUI;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void handlePageLoad() {
        final var users = userService.browseUsers();
        final var userViewModels = users.stream()
                .map(UserViewModel::fromUser)
                .toList();
        userListUI.showUsers(userViewModels);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSearchFieldChange(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return;
        }

        final var users = userService.browseUsers(searchText);
        final var userViewModels = users.stream()
                .map(UserViewModel::fromUser)
                .toList();
        userListUI.showUsers(userViewModels);
    }

    public void handleCreateUserButtonClick() {
        userListUI.navigateToCreateUserFormView();
    }

    public void handleEditUserButtonClick(@NonNull UserViewModel user) {
        final var updateUserRequest = createUpdateUserRequest(user);
        userListUI.navigateToEditUserFormView(updateUserRequest);
    }

    public void handleDeleteUserButtonClick(@NonNull UserViewModel user) {
        userListUI.showDeleteUserConfirmDialog(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleDeleteUserConfirm(@NonNull UserViewModel user) {
        userService.deleteUser(user.email());
        handlePageLoad();
    }

    private UpdateUserRequest createUpdateUserRequest(UserViewModel user) {
        final var updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName(user.firstName());
        updateUserRequest.setLastName(user.lastName());
        updateUserRequest.setEmail(user.email());
        return updateUserRequest;
    }

}
