package com.clean.elearning.user.usecase.model;

import com.clean.elearning.user.domain.Role;

public interface CreateUserModel {

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPassword();

    Role getRole();

}
