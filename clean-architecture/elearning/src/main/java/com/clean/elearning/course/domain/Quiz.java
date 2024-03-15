package com.clean.elearning.course.domain;

import com.clean.elearning.user.domain.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Quiz {

    private final Map<User, QuizResult> quizResults = new HashMap<>();
    private String name;
    private Instant openingTime;
    private Instant closingTime;
    private List<Question> questions;

    public Quiz(String name, Instant openingTime, Instant closingTime, List<Question> questions) {
        this(name, openingTime, closingTime, questions, new HashMap<>());
    }

    public Quiz(String name, Instant openingTime, Instant closingTime, List<Question> questions, Map<User, QuizResult> quizResults) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.questions = questions;
        this.quizResults.putAll(quizResults);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Instant openingTime) {
        this.openingTime = openingTime;
    }

    public Instant getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Instant closingTime) {
        this.closingTime = closingTime;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Map<User, QuizResult> getQuizResults() {
        return quizResults;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Quiz quiz)) {
            return false;
        }

        return Objects.equals(name, quiz.name);
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

}
