package com.clean.elearning.shared.service;

import com.clean.elearning.user.adapter.database.UserSchema;
import com.clean.elearning.user.domain.User;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public User getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserSchema.class)
                .map(UserSchema::toUser)
                .orElseThrow();
    }

    public void logout() {
        authenticationContext.logout();
    }

}
