package com.clean.elearning.course.adapter.ui.model;

import com.clean.elearning.course.domain.Quiz;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record QuizViewModel(
        String name,
        LocalDateTime openingTime,
        LocalDateTime closingTime,
        List<QuestionViewModel> questions
) {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static QuizViewModel fromQuiz(Quiz quiz) {
        final var questions = quiz.getQuestions().stream()
                .map(QuestionViewModel::fromQuestion)
                .toList();
        final var openingTime = LocalDateTime.ofInstant(quiz.getOpeningTime(), ZoneId.systemDefault());
        final var closingTime = LocalDateTime.ofInstant(quiz.getClosingTime(), ZoneId.systemDefault());
        return new QuizViewModel(quiz.getName(), openingTime, closingTime, questions);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT);
    }

}
