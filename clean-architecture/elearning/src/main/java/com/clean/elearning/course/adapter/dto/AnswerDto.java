package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.domain.Answer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {

    @NotBlank
    @Size(max = 255)
    private String content;
    private boolean isCorrect;

    public Answer toAnswer() {
        return new Answer(content, isCorrect);
    }

}
