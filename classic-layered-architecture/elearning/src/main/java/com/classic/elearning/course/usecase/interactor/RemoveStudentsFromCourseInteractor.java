package com.classic.elearning.course.usecase.interactor;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.usecase.RemoveStudentsFromCourseUseCase;
import com.classic.elearning.course.usecase.model.RemoveStudentsFromCourseModel;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;

public class RemoveStudentsFromCourseInteractor implements RemoveStudentsFromCourseUseCase {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public RemoveStudentsFromCourseInteractor(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void removeStudentsFromCourse(String name, RemoveStudentsFromCourseModel removeStudentsFromCourseModel) {
        final var course = getCourseByName(name);
        removeStudentsFromCourseModel.getStudentEmails().stream()
                .map(this::getUserByEmail)
                .forEach(course::removeStudent);
        courseRepository.saveCourse(course);
    }

    private Course getCourseByName(String name) {
        return courseRepository.getCourseByName(name).orElseThrow();
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }

}
