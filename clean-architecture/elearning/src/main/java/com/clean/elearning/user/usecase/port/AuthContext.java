package com.clean.elearning.user.usecase.port;

import com.clean.elearning.user.domain.User;

public interface AuthContext {

    User getCurrentUser();

}
