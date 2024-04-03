package com.classic.elearning.user.usecase;

import com.classic.elearning.user.domain.User;

import java.util.List;

public interface BrowseUsersUseCase {

    List<User> browseUsers();

    List<User> browseUsers(String searchText);

}
