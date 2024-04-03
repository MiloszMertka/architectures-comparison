package com.classic.elearning.course.adapter.dto;

import com.classic.elearning.course.domain.Question;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDto {

    @NotBlank
    @Size(max = 255)
    private String content;

    @Positive
    private double score;

    @NotNull
    @Size(min = 2)
    private List<@NotNull @Valid AnswerDto> answers = new ArrayList<>();

    public AnswerDto addAnswer() {
        final var answer = new AnswerDto();
        answers.add(answer);
        return answer;
    }

    public void removeAnswer(AnswerDto answer) {
        answers.remove(answer);
    }

    public Question toQuestion() {
        return new Question(content, score, answers.stream()
                .map(AnswerDto::toAnswer)
                .toList());
    }

}
