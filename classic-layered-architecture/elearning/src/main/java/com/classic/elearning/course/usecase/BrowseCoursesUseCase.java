package com.classic.elearning.course.usecase;

import com.classic.elearning.course.domain.Course;

import java.util.List;

public interface BrowseCoursesUseCase {

    List<Course> browseCourses();

    List<Course> browseCourses(String searchText);

}
