package com.clean.elearning.user.usecase.model;

import com.clean.elearning.user.domain.Role;

public interface UpdateUserModel {

    String getFirstName();

    String getLastName();

    String getEmail();

    Role getRole();

}
