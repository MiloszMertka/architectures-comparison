package com.clean.elearning.user.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSchemaRepository extends JpaRepository<UserSchema, Long> {

    Optional<UserSchema> findByEmail(String email);

}
