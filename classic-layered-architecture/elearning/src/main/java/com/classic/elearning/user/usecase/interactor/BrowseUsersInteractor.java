package com.classic.elearning.user.usecase.interactor;

import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.usecase.BrowseUsersUseCase;

import java.util.List;

public class BrowseUsersInteractor implements BrowseUsersUseCase {

    private final UserRepository userRepository;

    public BrowseUsersInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> browseUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public List<User> browseUsers(String searchText) {
        return userRepository.searchUsers(searchText);
    }

}
