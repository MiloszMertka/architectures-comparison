package com.classic.elearning.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double earnedScore;

    public QuizResult(Map<Question, Answer> solvedQuestions) {
        earnedScore = calculateEarnedScore(solvedQuestions);
    }

    private double calculateEarnedScore(Map<Question, Answer> solvedQuestions) {
        return solvedQuestions.entrySet().stream()
                .filter(solvedQuestion -> solvedQuestion.getValue().isCorrect())
                .mapToDouble(solvedQuestion -> solvedQuestion.getKey().getScore())
                .sum();
    }

}
