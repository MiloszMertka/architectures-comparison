package com.clean.elearning.course.usecase;

import com.clean.elearning.course.domain.Course;

import java.util.List;

public interface BrowseCoursesUseCase {

    List<Course> browseCourses();

    List<Course> browseCourses(String searchText);

}
