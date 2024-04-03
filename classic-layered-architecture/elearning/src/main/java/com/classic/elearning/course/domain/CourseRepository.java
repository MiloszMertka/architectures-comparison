package com.classic.elearning.course.domain;

import com.classic.elearning.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select course from Course course " +
            "where lower(course.name) like lower(concat('%', :searchText, '%')) " +
            "or lower(course.teacher.firstName) like lower(concat('%', :searchText, '%')) " +
            "or lower(course.teacher.lastName) like lower(concat('%', :searchText, '%'))")
    List<Course> search(@Param("searchText") String searchText);

    @Query("select course from Course course where :user member of course.students or course.teacher = :user")
    List<Course> findAllByUser(@Param("user") User user);

    Optional<Course> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);

}
