package com.clean.elearning.user.adapter;

import com.clean.elearning.user.infrastructure.UserListView;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserListPresenter {

    private final BrowseUsersUseCase browseUsersUseCase;

    @Setter
    private UserListView userListView;

    public void handlePageLoad() {
        final var users = browseUsersUseCase.browseUsers();
        final var userViewModels = users.stream()
                .map(UserViewModel::fromUser)
                .toList();
        userListView.showUsers(userViewModels);
    }

}
