package com.classic.elearning.shared.seeder;

import java.util.Set;

public interface Seeder {

    void seed();

    Set<Seeder> getDependencies();

}
