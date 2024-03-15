package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.*;
import com.clean.elearning.course.usecase.SolveQuizUseCase;
import com.clean.elearning.course.usecase.model.SolveQuizModel;
import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.usecase.port.AuthContext;

import java.util.Map;
import java.util.stream.Collectors;

public class SolveQuizInteractor implements SolveQuizUseCase {

    private final CourseRepository courseRepository;
    private final AuthContext authContext;

    public SolveQuizInteractor(CourseRepository courseRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.authContext = authContext;
    }

    @Override
    public void solveQuiz(String courseName, SolveQuizModel solveQuizModel) {
        final var course = getCourseByName(courseName);
        final var user = authContext.getCurrentUser();
        validateStudentIsEnrolled(course, user);
        final var quiz = course.getQuizByName(solveQuizModel.getQuizName());
        final var answeredQuestions = getAnsweredQuestionsByContents(quiz, solveQuizModel.getAnsweredQuestions());
        quiz.saveResults(user, answeredQuestions);
        courseRepository.saveCourse(course);
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.getCourseByName(courseName).orElseThrow();
    }

    private void validateStudentIsEnrolled(Course course, User student) {
        if (!course.isStudentEnrolled(student)) {
            throw new IllegalStateException("Student is not enrolled in the course");
        }
    }

    private Map<Question, Answer> getAnsweredQuestionsByContents(Quiz quiz, Map<String, String> answeredQuestions) {
        return answeredQuestions.entrySet().stream()
                .map(entry -> getAnsweredQuestionByContents(quiz, entry))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Question, Answer> getAnsweredQuestionByContents(Quiz quiz, Map.Entry<String, String> answeredQuestion) {
        final var question = getQuestionByContent(quiz, answeredQuestion.getKey());
        final var answer = getAnswerByContent(question, answeredQuestion.getValue());
        return Map.entry(question, answer);
    }

    private Question getQuestionByContent(Quiz quiz, String content) {
        return quiz.getQuestions().stream()
                .filter(question -> question.getContent().equals(content))
                .findFirst()
                .orElseThrow();
    }

    private Answer getAnswerByContent(Question question, String content) {
        return question.getAnswers().stream()
                .filter(answer -> answer.getContent().equals(content))
                .findFirst()
                .orElseThrow();
    }

}
