package com.classic.elearning.course.domain;

import com.classic.elearning.course.domain.Answer;
import com.classic.elearning.course.domain.Question;

import java.util.Map;
import java.util.Objects;

public class QuizResult {

    private final double earnedScore;

    public QuizResult(Map<com.classic.elearning.course.domain.Question, com.classic.elearning.course.domain.Answer> solvedQuestions) {
        earnedScore = calculateEarnedScore(solvedQuestions);
    }

    public QuizResult(double earnedScore) {
        this.earnedScore = earnedScore;
    }

    public double getEarnedScore() {
        return earnedScore;
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
