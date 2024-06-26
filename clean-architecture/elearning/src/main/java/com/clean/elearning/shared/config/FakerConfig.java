package com.clean.elearning.shared.config;

import com.github.javafaker.Faker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@ConditionalOnProperty(name = "seeder.enabled", havingValue = "true")
class FakerConfig {

    private static final long SEED = 123L;

    @Bean
    @ConditionalOnProperty(name = "seeder.enabled", havingValue = "true")
    public Faker faker() {
        final var random = new Random(SEED);
        return new Faker(random);
    }

}
