package com.classic.elearning.course.domain;

import java.util.Objects;

public class Answer {

    private final String content;
    private final boolean isCorrect;

    public Answer(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public String getContent() {
        return content;
    }

    public boolean isCorrect() {
        return isCorrect;
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

        if (!(object instanceof Answer answer)) {
            return false;
        }

        return Objects.equals(content, answer.content);
    }

}
