package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.BrowseCoursesUseCase;

import java.util.List;

public class BrowseCoursesInteractor implements BrowseCoursesUseCase {

    private final CourseRepository courseRepository;

    public BrowseCoursesInteractor(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> browseCourses() {
        return courseRepository.getAllCourses();
    }

    @Override
    public List<Course> browseCourses(String searchText) {
        return courseRepository.searchCourses(searchText);
    }

}
