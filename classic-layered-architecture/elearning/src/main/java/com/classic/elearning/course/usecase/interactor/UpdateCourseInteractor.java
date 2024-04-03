package com.classic.elearning.course.usecase.interactor;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.usecase.UpdateCourseUseCase;
import com.classic.elearning.course.usecase.model.UpdateCourseModel;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;

public class UpdateCourseInteractor implements UpdateCourseUseCase {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public UpdateCourseInteractor(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void updateCourse(String name, UpdateCourseModel updateCourseModel) {
        validateNameIsNotTaken(name, updateCourseModel.getName());
        final var course = getCourseByName(name);
        updateCourseFromModel(course, updateCourseModel);
        courseRepository.saveCourse(course);
    }

    private void validateNameIsNotTaken(String oldName, String newName) {
        if (!oldName.equals(newName) && courseRepository.courseExistsByName(newName)) {
            throw new IllegalStateException("Course with name " + newName + " already exists");
        }
    }

    private Course getCourseByName(String name) {
        return courseRepository.getCourseByName(name).orElseThrow();
    }

    private void updateCourseFromModel(Course course, UpdateCourseModel updateCourseModel) {
        final var teacher = getUserByEmail(updateCourseModel.getTeacherEmail());
        course.setName(updateCourseModel.getName());
        course.setTeacher(teacher);
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }

}
