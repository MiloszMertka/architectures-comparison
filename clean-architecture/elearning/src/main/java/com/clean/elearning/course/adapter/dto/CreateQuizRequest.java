package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.domain.Answer;
import com.clean.elearning.course.domain.Question;
import com.clean.elearning.course.usecase.model.CreateQuizModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateQuizRequest implements CreateQuizModel {

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
    private List<@NotNull @Valid QuestionData> questions = new ArrayList<>();

    @Override
    public List<Question> getQuestions() {
        return questions.stream()
                .map(QuestionData::toQuestion)
                .toList();
    }

    public QuestionData addQuestion() {
        final var question = new QuestionData();
        questions.add(question);
        return question;
    }

    public void removeQuestion(QuestionData question) {
        questions.remove(question);
    }

    @Getter
    @Setter
    public static class QuestionData {

        @NotBlank
        @Size(max = 255)
        private String content;

        @Positive
        private double score;

        @NotNull
        @Size(min = 2)
        private List<@NotNull @Valid AnswerData> answers = new ArrayList<>();

        public AnswerData addAnswer() {
            final var answer = new AnswerData();
            answers.add(answer);
            return answer;
        }

        public void removeAnswer(AnswerData answer) {
            answers.remove(answer);
        }

        protected Question toQuestion() {
            return new Question(content, score, answers.stream()
                    .map(AnswerData::toAnswer)
                    .toList());
        }

    }

    @Getter
    @Setter
    public static class AnswerData {

        @NotBlank
        @Size(max = 255)
        private String content;
        private boolean isCorrect;

        protected Answer toAnswer() {
            return new Answer(content, isCorrect);
        }

    }

}
