package com.classic.elearning.course.usecase.interactor;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.usecase.CreateCourseUseCase;
import com.classic.elearning.course.usecase.model.CreateCourseModel;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;

public class CreateCourseInteractor implements CreateCourseUseCase {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CreateCourseInteractor(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createCourse(CreateCourseModel createCourseModel) {
        validateCourseDoesNotExist(createCourseModel.getName());
        final var course = createCourseFromModel(createCourseModel);
        courseRepository.saveCourse(course);
    }

    private void validateCourseDoesNotExist(String name) {
        if (courseRepository.courseExistsByName(name)) {
            throw new IllegalStateException("Course with name " + name + " already exists");
        }
    }

    private Course createCourseFromModel(CreateCourseModel createCourseModel) {
        final var teacher = getUserByEmail(createCourseModel.getTeacherEmail());
        return new Course(createCourseModel.getName(), teacher);
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }

}
