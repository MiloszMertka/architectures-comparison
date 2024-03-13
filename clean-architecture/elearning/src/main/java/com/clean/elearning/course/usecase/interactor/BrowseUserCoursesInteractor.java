package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.BrowseUserCoursesUseCase;
import com.clean.elearning.user.usecase.port.AuthContext;

import java.util.List;

public class BrowseUserCoursesInteractor implements BrowseUserCoursesUseCase {

    private final CourseRepository courseRepository;
    private final AuthContext authContext;

    public BrowseUserCoursesInteractor(CourseRepository courseRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.authContext = authContext;
    }

    @Override
    public List<Course> browseUserCourses() {
        final var user = authContext.getCurrentUser();
        return courseRepository.getUserCourses(user);
    }

}
