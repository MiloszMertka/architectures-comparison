package com.clean.elearning.user.adapter;

import com.clean.elearning.user.usecase.PasswordHasher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PasswordHasherService implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
