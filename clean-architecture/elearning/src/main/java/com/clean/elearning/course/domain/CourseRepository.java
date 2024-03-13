package com.clean.elearning.course.domain;

import com.clean.elearning.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    List<Course> getAllCourses();

    List<Course> searchCourses(String searchText);

    List<Course> getUserCourses(User user);

    Optional<Course> getCourseByName(String name);

    boolean courseExistsByName(String name);

    void saveCourse(Course course);

    void deleteCourseByName(String name);

}
