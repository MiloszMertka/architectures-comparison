package com.classic.elearning.course.domain;

import com.classic.elearning.course.domain.Answer;

import java.util.List;
import java.util.Objects;

public class Question {

    private final String content;
    private final double score;
    private final List<Answer> answers;

    public Question(String content, double score, List<Answer> answers) {
        this.content = content;
        this.score = score;
        this.answers = answers;
    }

    public String getContent() {
        return content;
    }

    public double getScore() {
        return score;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Question question)) {
            return false;
        }

        return Objects.equals(content, question.content);
    }

}
