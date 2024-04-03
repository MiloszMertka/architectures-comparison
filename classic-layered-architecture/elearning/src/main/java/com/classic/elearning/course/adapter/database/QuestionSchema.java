package com.classic.elearning.course.adapter.database;

import com.classic.elearning.course.adapter.database.AnswerSchema;
import com.classic.elearning.course.domain.Question;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuestionSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double score;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<com.classic.elearning.course.adapter.database.AnswerSchema> answers;

    public static QuestionSchema fromQuestion(@NonNull Question question) {
        final var answers = question.getAnswers().stream()
                .map(com.classic.elearning.course.adapter.database.AnswerSchema::fromAnswer)
                .toList();
        return new QuestionSchema(null, question.getContent(), question.getScore(), answers);
    }

    public Question toQuestion() {
        final var domainAnswers = answers.stream()
                .map(AnswerSchema::toAnswer)
                .toList();
        return new Question(content, score, domainAnswers);
    }

}
