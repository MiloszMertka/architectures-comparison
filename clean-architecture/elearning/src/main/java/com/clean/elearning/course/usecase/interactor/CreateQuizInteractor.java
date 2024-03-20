package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Answer;
import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.domain.Quiz;
import com.clean.elearning.course.usecase.CreateQuizUseCase;
import com.clean.elearning.course.usecase.model.CreateQuizModel;
import com.clean.elearning.user.usecase.port.AuthContext;

import java.time.Instant;
import java.time.ZoneId;

public class CreateQuizInteractor implements CreateQuizUseCase {

    private final CourseRepository courseRepository;
    private final AuthContext authContext;

    public CreateQuizInteractor(CourseRepository courseRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.authContext = authContext;
    }

    @Override
    public void createQuiz(String courseName, CreateQuizModel createQuizModel) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        validateQuizNameIsUnique(course, createQuizModel.getName());
        final var openingTime = createQuizModel.getOpeningTime().atZone(ZoneId.systemDefault()).toInstant();
        final var closingTime = createQuizModel.getClosingTime().atZone(ZoneId.systemDefault()).toInstant();
        validateClosingTimeIsAfterOpeningTime(closingTime, openingTime);
        validateEveryQuestionHasAtLeastOneCorrectAnswer(createQuizModel);
        final var quiz = new Quiz(createQuizModel.getName(), openingTime, closingTime, createQuizModel.getQuestions());
        course.addQuiz(quiz);
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

    private void validateQuizNameIsUnique(Course course, String quizName) {
        if (course.getQuizzes().stream().anyMatch(quiz -> quiz.getName().equals(quizName))) {
            throw new IllegalStateException("Quiz name is not unique");
        }
    }

    private void validateClosingTimeIsAfterOpeningTime(Instant closingTime, Instant openingTime) {
        if (closingTime.isBefore(openingTime)) {
            throw new IllegalStateException("Closing time is before opening time");
        }
    }

    private void validateEveryQuestionHasAtLeastOneCorrectAnswer(CreateQuizModel createQuizModel) {
        if (createQuizModel.getQuestions().stream()
                .anyMatch(question -> question.getAnswers().stream()
                        .noneMatch(Answer::isCorrect)
                )
        ) {
            throw new IllegalStateException("Every question must have at least one correct answer");
        }
    }

}
