package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.domain.Question;
import com.clean.elearning.course.usecase.model.UpdateQuizModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateQuizRequest implements UpdateQuizModel, QuizRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Future
    private LocalDateTime openingTime;

    @NotNull
    @Future
    private LocalDateTime closingTime;

    @NotNull
    @Size(min = 1)
    private List<@NotNull @Valid QuestionDto> questions = new ArrayList<>();

    @Override
    public List<Question> getQuestions() {
        return questions.stream()
                .map(QuestionDto::toQuestion)
                .toList();
    }

    public List<QuestionDto> getQuestionDtos() {
        return questions;
    }

    public QuestionDto addQuestion() {
        final var question = new QuestionDto();
        questions.add(question);
        return question;
    }

    public void removeQuestion(QuestionDto question) {
        questions.remove(question);
    }

}
