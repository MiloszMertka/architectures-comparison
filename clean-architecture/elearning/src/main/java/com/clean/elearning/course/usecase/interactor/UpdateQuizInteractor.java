package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Answer;
import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.domain.Quiz;
import com.clean.elearning.course.usecase.UpdateQuizUseCase;
import com.clean.elearning.course.usecase.model.UpdateQuizModel;
import com.clean.elearning.user.usecase.port.AuthContext;

import java.time.Instant;
import java.time.ZoneId;

public class UpdateQuizInteractor implements UpdateQuizUseCase {

    private final CourseRepository courseRepository;
    private final AuthContext authContext;

    public UpdateQuizInteractor(CourseRepository courseRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.authContext = authContext;
    }

    @Override
    public void updateQuiz(String courseName, String quizName, UpdateQuizModel updateQuizModel) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        validateNameIsNotTaken(course, quizName, updateQuizModel.getName());
        validateEveryQuestionHasAtLeastOneCorrectAnswer(updateQuizModel);
        final var quiz = getQuizByName(course, updateQuizModel.getName());
        updateQuizFromModel(quiz, updateQuizModel);
        courseRepository.saveCourse(course);
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.getCourseByName(courseName).orElseThrow();
    }

    private Quiz getQuizByName(Course course, String quizName) {
        return course.getQuizzes().stream()
                .filter(quiz -> quiz.getName().equals(quizName))
                .findFirst()
                .orElseThrow();
    }

    private void updateQuizFromModel(Quiz quiz, UpdateQuizModel updateQuizModel) {
        final var openingTime = updateQuizModel.getOpeningTime().atZone(ZoneId.systemDefault()).toInstant();
        final var closingTime = updateQuizModel.getClosingTime().atZone(ZoneId.systemDefault()).toInstant();
        validateClosingTimeIsAfterOpeningTime(closingTime, openingTime);
        quiz.setName(updateQuizModel.getName());
        quiz.setOpeningTime(openingTime);
        quiz.setClosingTime(closingTime);
        quiz.setQuestions(updateQuizModel.getQuestions());
    }

    private void validateTeacherOwnsCourse(Course course) {
        if (!course.getTeacher().equals(authContext.getCurrentUser())) {
            throw new IllegalStateException("Teacher does not own course");
        }
    }

    private void validateNameIsNotTaken(Course course, String oldName, String newName) {
        if (course.getQuizzes().stream()
                .anyMatch(quiz -> !quiz.getName().equals(oldName) && quiz.getName().equals(newName))
        ) {
            throw new IllegalStateException("Quiz with name " + newName + " already exists");
        }
    }

    private void validateClosingTimeIsAfterOpeningTime(Instant closingTime, Instant openingTime) {
        if (closingTime.isBefore(openingTime)) {
            throw new IllegalStateException("Closing time is before opening time");
        }
    }

    private void validateEveryQuestionHasAtLeastOneCorrectAnswer(UpdateQuizModel updateQuizModel) {
        if (updateQuizModel.getQuestions().stream()
                .anyMatch(question -> question.getAnswers().stream()
                        .noneMatch(Answer::isCorrect)
                )
        ) {
            throw new IllegalStateException("Every question must have at least one correct answer");
        }
    }

}
