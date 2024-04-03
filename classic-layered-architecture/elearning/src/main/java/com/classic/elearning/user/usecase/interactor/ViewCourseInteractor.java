package com.classic.elearning.user.usecase.interactor;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.user.usecase.ViewCourseUseCase;

public class ViewCourseInteractor implements ViewCourseUseCase {

    private final CourseRepository courseRepository;

    public ViewCourseInteractor(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course viewCourse(String courseName) {
        return courseRepository.getCourseByName(courseName).orElseThrow();
    }

}
