package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.DeleteQuizUseCase;
import com.clean.elearning.user.usecase.port.AuthContext;

public class DeleteQuizInteractor implements DeleteQuizUseCase {

    private final CourseRepository courseRepository;
    private final AuthContext authContext;

    public DeleteQuizInteractor(CourseRepository courseRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.authContext = authContext;
    }

    @Override
    public void deleteQuiz(String courseName, String quizName) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        final var quiz = course.getQuizByName(quizName);
        course.removeQuiz(quiz);
        courseRepository.saveCourse(course);
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.getCourseByName(courseName).orElseThrow();
    }

    private void validateTeacherOwnsCourse(Course course) {
        if (!course.getTeacher().equals(authContext.getCurrentUser())) {
            throw new IllegalStateException("Teacher does not own course");
        }
    }

}
