package com.classic.elearning.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user " +
            "where lower(user.firstName) like lower(concat('%', :searchText, '%')) " +
            "or lower(user.lastName) like lower(concat('%', :searchText, '%')) " +
            "or lower(user.email) like lower(concat('%', :searchText, '%')) ")
    List<User> search(@Param("searchText") String searchText);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

}
