package com.classic.elearning.user.infrastructure;

import com.classic.elearning.shared.seeder.Seeder;
import com.classic.elearning.user.domain.Role;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.usecase.port.PasswordHasher;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(name = "seeder.enabled", havingValue = "true")
public class UserSeeder implements Seeder {

    private static final int USERS_TO_SEED = 100;
    private static final String USERS_PASSWORD = "ZAQ!2wsx";
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final Faker faker;

    @Override
    public void seed() {
        final Set<User> users = new HashSet<>();

        while (users.size() < USERS_TO_SEED) {
            final var user = createRandomUser();

            if (!users.contains(user)) {
                users.add(user);
                userRepository.saveUser(user);
            }
        }
    }

    @Override
    public Set<Seeder> getDependencies() {
        return Set.of();
    }

    private User createRandomUser() {
        return new User(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                passwordHasher.hashPassword(USERS_PASSWORD),
                faker.options().option(Role.class)
        );
    }

}
