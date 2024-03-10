package com.clean.elearning.user.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAllUsers();

    List<User> searchUsers(String searchText);

    Optional<User> getUserByEmail(String email);

    boolean userExistsByEmail(String email);

    void saveUser(User user);

    void deleteUserByEmail(String email);

}
