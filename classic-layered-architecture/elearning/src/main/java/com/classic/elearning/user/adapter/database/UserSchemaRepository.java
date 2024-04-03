package com.classic.elearning.user.adapter.database;

import com.classic.elearning.user.adapter.database.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSchemaRepository extends JpaRepository<com.classic.elearning.user.adapter.database.UserSchema, Long> {

    @Query("select user from UserSchema user " +
            "where lower(user.firstName) like lower(concat('%', :searchText, '%')) " +
            "or lower(user.lastName) like lower(concat('%', :searchText, '%')) " +
            "or lower(user.email) like lower(concat('%', :searchText, '%')) ")
    List<com.classic.elearning.user.adapter.database.UserSchema> search(@Param("searchText") String searchText);

    Optional<com.classic.elearning.user.adapter.database.UserSchema> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

}
