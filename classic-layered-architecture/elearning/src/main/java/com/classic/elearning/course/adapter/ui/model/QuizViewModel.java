package com.classic.elearning.course.adapter.ui.model;

import com.classic.elearning.course.domain.Quiz;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record QuizViewModel(
        String name,
        LocalDateTime openingTime,
        LocalDateTime closingTime,
        List<com.classic.elearning.course.adapter.ui.model.QuestionViewModel> questions,
        List<QuizResultViewModel> quizResults,
        String totalScore
) {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static QuizViewModel fromQuiz(Quiz quiz) {
        final var questions = quiz.getQuestions().stream()
                .map(QuestionViewModel::fromQuestion)
                .toList();
        final var openingTime = LocalDateTime.ofInstant(quiz.getOpeningTime(), ZoneId.systemDefault());
        final var closingTime = LocalDateTime.ofInstant(quiz.getClosingTime(), ZoneId.systemDefault());
        final var quizResults = quiz.getQuizResults().entrySet().stream()
                .map(entry -> QuizResultViewModel.fromStudentAndQuizResult(entry.getKey(), entry.getValue()))
                .toList();
        final var totalScore = String.format("%.2f", quiz.getTotalScore());
        return new QuizViewModel(quiz.getName(), openingTime, closingTime, questions, quizResults, totalScore);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT);
    }

    public boolean isSolvedByStudent(UserViewModel student) {
        return quizResults.stream().anyMatch(quizResult -> quizResult.student().equals(student));
    }

    public QuizResultViewModel getQuizResultByStudent(UserViewModel student) {
        return quizResults.stream()
                .filter(quizResult -> quizResult.student().equals(student))
                .findFirst()
                .orElseThrow();
    }

}
