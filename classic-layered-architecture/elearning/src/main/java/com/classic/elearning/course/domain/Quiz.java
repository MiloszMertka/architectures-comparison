package com.classic.elearning.course.domain;

import com.classic.elearning.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant openingTime;

    @Column(nullable = false)
    private Instant closingTime;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<User, QuizResult> quizResults = new HashMap<>();

    public Quiz(String name, Instant openingTime, Instant closingTime, List<Question> questions) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.questions = questions;
    }

    public static String formatDateTime(Instant time) {
        final var dateTime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        return dateTime.format(DATE_TIME_FORMAT);
    }

    public double getTotalScore() {
        return questions.stream()
                .mapToDouble(Question::getScore)
                .sum();
    }

    public void saveResults(User student, Map<Question, Answer> solvedQuestions) {
        validateStudentCanSaveResults(student);
        final var quizResult = new QuizResult(solvedQuestions);
        quizResults.put(student, quizResult);
    }

    private void validateStudentCanSaveResults(User student) {
        if (quizResults.containsKey(student)) {
            throw new IllegalStateException("Student already solved the quiz");
        }
    }

    public boolean isSolvedByStudent(User student) {
        return quizResults.entrySet().stream()
                .anyMatch(quizResult -> quizResult.getKey().equals(student));
    }

    public QuizResult getQuizResultByStudent(User student) {
        return quizResults.entrySet().stream()
                .filter(quizResult -> quizResult.getKey().equals(student))
                .findFirst()
                .orElseThrow()
                .getValue();
    }

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
    }

}
