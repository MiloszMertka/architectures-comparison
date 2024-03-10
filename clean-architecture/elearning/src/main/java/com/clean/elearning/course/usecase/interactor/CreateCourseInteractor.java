package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.CreateCourseUseCase;
import com.clean.elearning.course.usecase.model.CreateCourseModel;
import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.domain.UserRepository;

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
