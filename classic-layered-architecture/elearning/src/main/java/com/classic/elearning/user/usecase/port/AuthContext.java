package com.classic.elearning.user.usecase.port;

import com.classic.elearning.user.domain.User;

public interface AuthContext {

    User getCurrentUser();

}
