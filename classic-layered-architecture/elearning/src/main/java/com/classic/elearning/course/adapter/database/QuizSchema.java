package com.classic.elearning.course.adapter.database;

import com.classic.elearning.course.adapter.database.QuestionSchema;
import com.classic.elearning.course.adapter.database.QuizResultSchema;
import com.classic.elearning.course.domain.Quiz;
import com.classic.elearning.user.adapter.database.UserSchema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant openingTime;

    @Column(nullable = false)
    private Instant closingTime;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<QuestionSchema> questions;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Map<UserSchema, QuizResultSchema> quizResults;

    public static QuizSchema fromQuiz(@NonNull Quiz quiz) {
        final var questions = quiz.getQuestions().stream()
                .map(QuestionSchema::fromQuestion)
                .toList();
        final var quizResults = quiz.getQuizResults().entrySet().stream()
                .map(entry -> Map.entry(UserSchema.fromUser(entry.getKey()), QuizResultSchema.fromQuizResult(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new QuizSchema(null, quiz.getName(), quiz.getOpeningTime(), quiz.getClosingTime(), questions, quizResults);
    }

    public Quiz toQuiz() {
        final var domainQuestions = questions.stream()
                .map(QuestionSchema::toQuestion)
                .toList();
        final var domainQuizResults = quizResults.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().toUser(), entry.getValue().toQuizResult()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Quiz(name, openingTime, closingTime, domainQuestions, domainQuizResults);
    }

}
