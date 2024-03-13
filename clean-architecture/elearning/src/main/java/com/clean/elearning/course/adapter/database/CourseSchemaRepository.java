package com.clean.elearning.course.adapter.database;

import com.clean.elearning.user.adapter.database.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseSchemaRepository extends JpaRepository<CourseSchema, Long> {

    @Query("select course from CourseSchema course " +
            "where lower(course.name) like lower(concat('%', :searchText, '%')) " +
            "or lower(course.teacher.firstName) like lower(concat('%', :searchText, '%')) " +
            "or lower(course.teacher.lastName) like lower(concat('%', :searchText, '%'))")
    List<CourseSchema> search(@Param("searchText") String searchText);

    @Query("select course from CourseSchema course where :user member of course.students or course.teacher = :user")
    List<CourseSchema> findAllByUser(@Param("user") UserSchema user);

    Optional<CourseSchema> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);

}
