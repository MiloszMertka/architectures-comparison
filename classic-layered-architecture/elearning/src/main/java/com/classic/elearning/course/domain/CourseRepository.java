package com.classic.elearning.course.domain;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    List<com.classic.elearning.course.domain.Course> getAllCourses();

    List<com.classic.elearning.course.domain.Course> searchCourses(String searchText);

    List<com.classic.elearning.course.domain.Course> getUserCourses(User user);

    Optional<com.classic.elearning.course.domain.Course> getCourseByName(String name);

    boolean courseExistsByName(String name);

    void saveCourse(Course course);

    void deleteCourseByName(String name);

}
