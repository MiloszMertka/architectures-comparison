package com.clean.elearning.user.infrastructure;

import com.clean.elearning.user.domain.UserRepository;
import com.clean.elearning.user.usecase.BrowseUsersInteractor;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserConfig {

    @Bean
    public BrowseUsersUseCase browseUsersUseCaseInput(UserRepository userRepository) {
        return new BrowseUsersInteractor(userRepository);
    }

}
