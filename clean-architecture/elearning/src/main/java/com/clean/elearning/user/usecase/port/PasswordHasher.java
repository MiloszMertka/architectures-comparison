package com.clean.elearning.user.usecase.port;

public interface PasswordHasher {

    String hashPassword(String password);

    boolean verifyPasswordsMatch(String rawPassword, String hashedPassword);

}
