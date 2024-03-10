package com.clean.elearning.course.domain;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    List<Course> getAllCourses();

    List<Course> searchCourses(String searchText);

    Optional<Course> getCourseByName(String name);

    boolean courseExistsByName(String name);

    void saveCourse(Course course);

    void deleteCourseByName(String name);

}
