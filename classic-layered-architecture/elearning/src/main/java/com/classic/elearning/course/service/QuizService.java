package com.classic.elearning.course.service;

import com.classic.elearning.course.domain.*;
import com.classic.elearning.course.infrastructure.dto.CreateQuizRequest;
import com.classic.elearning.course.infrastructure.dto.SolveQuizRequest;
import com.classic.elearning.course.infrastructure.dto.UpdateQuizRequest;
import com.classic.elearning.user.domain.User;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class QuizService {

    private final CourseRepository courseRepository;
    private final AuthenticationContext authenticationContext;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('TEACHER')")
    public Map<User, QuizResult> browseQuizResults(String courseName, String quizName) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        final var quiz = getQuizByName(course, quizName);
        return quiz.getQuizResults();
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void createQuiz(String courseName, CreateQuizRequest createQuizRequest) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        validateQuizNameIsUnique(course, createQuizRequest.getName());
        final var openingTime = createQuizRequest.getOpeningTime().atZone(ZoneId.systemDefault()).toInstant();
        final var closingTime = createQuizRequest.getClosingTime().atZone(ZoneId.systemDefault()).toInstant();
        validateClosingTimeIsAfterOpeningTime(closingTime, openingTime);
        validateEveryQuestionHasAtLeastOneCorrectAnswer(createQuizRequest);
        final var quiz = new Quiz(createQuizRequest.getName(), openingTime, closingTime, createQuizRequest.getQuestions());
        course.addQuiz(quiz);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void updateQuiz(String courseName, String quizName, UpdateQuizRequest updateQuizRequest) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        validateNameIsNotTaken(course, quizName, updateQuizRequest.getName());
        validateEveryQuestionHasAtLeastOneCorrectAnswer(updateQuizRequest);
        final var quiz = getQuizByName(course, updateQuizRequest.getName());
        updateQuizFromRequest(quiz, updateQuizRequest);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void deleteQuiz(String courseName, String quizName) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        final var quiz = getQuizByName(course, quizName);
        course.removeQuiz(quiz);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void solveQuiz(String courseName, SolveQuizRequest solveQuizRequest) {
        final var course = getCourseByName(courseName);
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        validateStudentIsEnrolled(course, user);
        final var quiz = getQuizByName(course, solveQuizRequest.getQuizName());
        final var answeredQuestions = getAnsweredQuestionsByContents(quiz, solveQuizRequest.getAnsweredQuestions());
        quiz.saveResults(user, answeredQuestions);
        courseRepository.save(course);
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

    private void updateQuizFromRequest(Quiz quiz, UpdateQuizRequest updateQuizRequest) {
        final var openingTime = updateQuizRequest.getOpeningTime().atZone(ZoneId.systemDefault()).toInstant();
        final var closingTime = updateQuizRequest.getClosingTime().atZone(ZoneId.systemDefault()).toInstant();
        validateClosingTimeIsAfterOpeningTime(closingTime, openingTime);
        quiz.setName(updateQuizRequest.getName());
        quiz.setOpeningTime(openingTime);
        quiz.setClosingTime(closingTime);
        quiz.setQuestions(updateQuizRequest.getQuestions());
    }

    private void validateNameIsNotTaken(Course course, String oldName, String newName) {
        if (course.getQuizzes().stream()
                .anyMatch(quiz -> !quiz.getName().equals(oldName) && quiz.getName().equals(newName))
        ) {
            throw new IllegalStateException("Quiz with name " + newName + " already exists");
        }
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.findByName(courseName).orElseThrow();
    }

    private void validateTeacherOwnsCourse(Course course) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!course.getTeacher().equals(user)) {
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

    private void validateEveryQuestionHasAtLeastOneCorrectAnswer(CreateQuizRequest createQuizRequest) {
        if (createQuizRequest.getQuestions().stream()
                .anyMatch(question -> question.getAnswers().stream()
                        .noneMatch(Answer::isCorrect)
                )
        ) {
            throw new IllegalStateException("Every question must have at least one correct answer");
        }
    }

    private void validateEveryQuestionHasAtLeastOneCorrectAnswer(UpdateQuizRequest updateQuizRequest) {
        if (updateQuizRequest.getQuestions().stream()
                .anyMatch(question -> question.getAnswers().stream()
                        .noneMatch(Answer::isCorrect)
                )
        ) {
            throw new IllegalStateException("Every question must have at least one correct answer");
        }
    }

    private Quiz getQuizByName(Course course, String quizName) {
        return course.getQuizzes().stream()
                .filter(quiz -> quiz.getName().equals(quizName))
                .findFirst()
                .orElseThrow();
    }

}
