package com.clean.elearning.course.domain;

import java.util.Map;
import java.util.Objects;

public class QuizResult {

    private final double earnedScore;

    public QuizResult(Map<Question, Answer> solvedQuestions) {
        earnedScore = calculateEarnedScore(solvedQuestions);
    }

    public QuizResult(double earnedScore) {
        this.earnedScore = earnedScore;
    }

    public double getEarnedScore() {
        return earnedScore;
    }

    public int calculatePercentageCorrectness() {
        return Math.toIntExact(Math.round(earnedScore / 100));
    }

    @Override
    public int hashCode() {
        return Objects.hash(earnedScore);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof QuizResult that)) {
            return false;
        }

        return Objects.equals(earnedScore, that.earnedScore);
    }

    private double calculateEarnedScore(Map<Question, Answer> solvedQuestions) {
        return solvedQuestions.entrySet().stream()
                .filter(solvedQuestion -> solvedQuestion.getValue().isCorrect())
                .mapToDouble(solvedQuestion -> solvedQuestion.getKey().getScore())
                .sum();
    }

}
