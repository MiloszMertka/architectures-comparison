package com.clean.elearning.user.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.user.usecase.ViewCourseUseCase;

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
