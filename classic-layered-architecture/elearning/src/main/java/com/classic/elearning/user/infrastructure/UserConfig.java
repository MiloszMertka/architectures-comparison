package com.classic.elearning.user.infrastructure;

import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.usecase.*;
import com.classic.elearning.user.usecase.interactor.*;
import com.classic.elearning.user.usecase.port.AuthContext;
import com.classic.elearning.user.usecase.port.PasswordHasher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserConfig {

    @Bean
    public BrowseUsersUseCase browseUsersUseCase(UserRepository userRepository) {
        return new BrowseUsersInteractor(userRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new CreateUserInteractor(userRepository, passwordHasher);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository) {
        return new UpdateUserInteractor(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserInteractor(userRepository);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository, PasswordHasher passwordHasher, AuthContext authContext) {
        return new ChangePasswordInteractor(userRepository, passwordHasher, authContext);
    }

}
