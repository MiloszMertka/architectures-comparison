package com.clean.elearning.user.adapter.ui.model;

import com.clean.elearning.user.domain.User;
import org.springframework.lang.NonNull;

public record UserViewModel(
        String firstName,
        String lastName,
        String email,
        String role
) {

    public static UserViewModel fromUser(@NonNull User user) {
        return new UserViewModel(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name().toLowerCase()
        );
    }

}
