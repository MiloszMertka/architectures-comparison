package com.clean.elearning.user.adapter.ui;

import com.clean.elearning.user.adapter.dto.CreateUserRequest;
import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.domain.Role;
import com.clean.elearning.user.infrastructure.view.UserListView;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
import com.clean.elearning.user.usecase.CreateUserUseCase;
import com.clean.elearning.user.usecase.UpdateUserUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserListPresenter {

    private final BrowseUsersUseCase browseUsersUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @Setter
    private UserListView userListView;

    @Transactional(readOnly = true)
    public void handlePageLoad() {
        final var users = browseUsersUseCase.browseUsers();
        final var userViewModels = users.stream()
                .map(UserViewModel::fromUser)
                .toList();
        userListView.showUsers(userViewModels);
    }

    @Transactional
    public void handleSaveUser(@NonNull CreateUserRequest createUserRequest) {
        createUserUseCase.createUser(createUserRequest);
        handlePageLoad();
    }

    @Transactional
    public void handleSaveUser(@NonNull UpdateUserRequest updateUserRequest) {
        updateUserUseCase.updateUser(updateUserRequest);
        handlePageLoad();
    }

    public void handleUserSelection(UserViewModel userViewModel) {
        if (userViewModel == null) {
            userListView.getEditUserFormComponent().closeEditor();
            return;
        }

        final var updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName(userViewModel.firstName());
        updateUserRequest.setLastName(userViewModel.lastName());
        updateUserRequest.setEmail(userViewModel.email());
        updateUserRequest.setRole(Role.valueOf(userViewModel.role().toUpperCase()));

        userListView.getEditUserFormComponent().setUser(updateUserRequest);
        userListView.getEditUserFormComponent().openEditor();
    }

}
