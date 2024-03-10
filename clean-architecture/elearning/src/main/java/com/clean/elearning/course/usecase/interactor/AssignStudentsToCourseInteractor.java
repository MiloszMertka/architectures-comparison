package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.AssignStudentsToCourseUseCase;
import com.clean.elearning.course.usecase.model.AssignStudentsToCourseModel;
import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.domain.UserRepository;

public class AssignStudentsToCourseInteractor implements AssignStudentsToCourseUseCase {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public AssignStudentsToCourseInteractor(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void assignStudentsToCourse(String name, AssignStudentsToCourseModel assignStudentsToCourseModel) {
        final var course = getCourseByName(name);
        assignStudentsToCourseModel.getStudentEmails().stream()
                .map(this::getUserByEmail)
                .forEach(course::assignStudent);
    }

    private Course getCourseByName(String name) {
        return courseRepository.getCourseByName(name).orElseThrow();
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }

}
