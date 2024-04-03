package com.classic.elearning.user.usecase.model;

import com.classic.elearning.user.domain.Role;

public interface CreateUserModel {

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPassword();

    Role getRole();

}
