package com.clean.elearning.user.adapter.ui;

import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.infrastructure.view.UserListView;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
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

    public void handleEditUserButtonClick(@NonNull UserViewModel user) {
        final var updateUserRequest = createUpdateUserRequest(user);
        userListView.navigateToEditUserFormView(updateUserRequest);
    }

    @Transactional
    public void handleDeleteUserButtonClick(@NonNull UserViewModel user) {
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
