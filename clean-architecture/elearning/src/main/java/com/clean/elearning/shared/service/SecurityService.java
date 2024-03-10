package com.clean.elearning.shared.service;

import com.clean.elearning.user.adapter.database.UserSchema;
import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.usecase.port.AuthContext;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityService implements AuthContext {

    private final AuthenticationContext authenticationContext;

    @Override
    public User getCurrentUser() {
        return authenticationContext.getAuthenticatedUser(UserSchema.class)
                .map(UserSchema::toUser)
                .orElseThrow();
    }

    public void logout() {
        authenticationContext.logout();
    }

}
