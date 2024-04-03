package com.classic.elearning.course.adapter.ui.model;

import com.clean.elearning.course.domain.Question;

import java.util.List;

public record QuestionViewModel(
        String content,
        double score,
        List<AnswerViewModel> answers
) {

    public static QuestionViewModel fromQuestion(Question question) {
        final var answers = question.getAnswers().stream()
                .map(AnswerViewModel::fromAnswer)
                .toList();
        return new QuestionViewModel(question.getContent(), question.getScore(), answers);
    }

}
