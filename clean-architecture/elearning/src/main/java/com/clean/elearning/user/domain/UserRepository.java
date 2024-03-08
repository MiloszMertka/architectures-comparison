package com.clean.elearning.user.domain;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    boolean userExistsByEmail(String email);

    void saveUser(User user);

}
