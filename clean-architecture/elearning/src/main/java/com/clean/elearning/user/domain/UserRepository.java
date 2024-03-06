package com.clean.elearning.user.domain;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    void saveUser(User user);

}
