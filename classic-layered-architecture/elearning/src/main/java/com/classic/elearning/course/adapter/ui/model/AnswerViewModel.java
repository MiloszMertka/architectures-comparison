package com.classic.elearning.course.adapter.ui.model;

import com.clean.elearning.course.domain.Answer;

public record AnswerViewModel(
        String content,
        boolean isCorrect
) {

    public static AnswerViewModel fromAnswer(Answer answer) {
        return new AnswerViewModel(answer.getContent(), answer.isCorrect());
    }

}
