package com.clean.elearning.user.infrastructure;

import com.clean.elearning.user.domain.UserRepository;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
import com.clean.elearning.user.usecase.CreateUserUseCase;
import com.clean.elearning.user.usecase.UpdateUserUseCase;
import com.clean.elearning.user.usecase.interactor.BrowseUsersInteractor;
import com.clean.elearning.user.usecase.interactor.CreateUserInteractor;
import com.clean.elearning.user.usecase.interactor.UpdateUserInteractor;
import com.clean.elearning.user.usecase.port.PasswordHasher;
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

}
