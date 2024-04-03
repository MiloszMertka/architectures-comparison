package com.classic.elearning.course.usecase.interactor;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.domain.Quiz;
import com.classic.elearning.course.domain.QuizResult;
import com.classic.elearning.course.usecase.BrowseQuizResultsUseCase;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.usecase.port.AuthContext;

import java.util.Map;

public class BrowseQuizResultsInteractor implements BrowseQuizResultsUseCase {

    private final CourseRepository courseRepository;
    private final AuthContext authContext;

    public BrowseQuizResultsInteractor(CourseRepository courseRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.authContext = authContext;
    }

    @Override
    public Map<User, QuizResult> browseQuizResults(String courseName, String quizName) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        final var quiz = getQuizByName(course, quizName);
        return quiz.getQuizResults();
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.getCourseByName(courseName).orElseThrow();
    }

    private void validateTeacherOwnsCourse(Course course) {
        if (!course.getTeacher().equals(authContext.getCurrentUser())) {
            throw new IllegalStateException("Teacher does not own course");
        }
    }

    private Quiz getQuizByName(Course course, String quizName) {
        return course.getQuizzes().stream()
                .filter(quiz -> quiz.getName().equals(quizName))
                .findFirst()
                .orElseThrow();
    }

}
