package com.clean.elearning.user.usecase;

import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.domain.UserRepository;

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

}
