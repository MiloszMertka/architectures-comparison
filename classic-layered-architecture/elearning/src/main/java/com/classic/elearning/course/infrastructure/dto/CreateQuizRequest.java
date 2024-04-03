package com.classic.elearning.course.infrastructure.dto;

import com.classic.elearning.course.domain.Question;
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
public class CreateQuizRequest implements QuizRequest {

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

    public QuestionDto addQuestion() {
        final var question = new QuestionDto();
        questions.add(question);
        return question;
    }

    public void removeQuestion(QuestionDto question) {
        questions.remove(question);
    }

}
