package com.classic.elearning.shared.seeder;

import com.classic.elearning.shared.seeder.Seeder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(name = "seeder.enabled", havingValue = "true")
class DevelopmentDatabaseSeeder implements CommandLineRunner {

    private static final String CIRCULAR_DEPENDENCIES_ERROR_MESSAGE = "Circular dependencies detected while seeding the database";
    private final Set<Seeder> seeders;

    @Override
    public void run(String... args) {
        final Set<Seeder> doneSeeders = new HashSet<>();

        while (doneSeeders.size() < seeders.size()) {
            if (!tryToRunSeeders(seeders, doneSeeders)) {
                throw new IllegalStateException(CIRCULAR_DEPENDENCIES_ERROR_MESSAGE);
            }
        }
    }

    private boolean tryToRunSeeders(Set<Seeder> seeders, Set<Seeder> doneSeeders) {
        var somethingSeeded = false;

        for (final var seeder : seeders) {
            if (checkDependenciesMet(seeder, doneSeeders)) {
                seeder.seed();
                somethingSeeded = true;
                doneSeeders.add(seeder);
            }
        }

        return somethingSeeded;
    }

    private boolean checkDependenciesMet(Seeder seeder, Set<Seeder> doneSeeders) {
        return doneSeeders.containsAll(seeder.getDependencies());
    }

}
