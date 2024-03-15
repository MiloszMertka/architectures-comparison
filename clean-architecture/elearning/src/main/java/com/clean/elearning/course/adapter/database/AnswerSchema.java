package com.clean.elearning.course.adapter.database;

import com.clean.elearning.course.domain.Answer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnswerSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isCorrect;

    public static AnswerSchema fromAnswer(@NonNull Answer answer) {
        return new AnswerSchema(null, answer.getContent(), answer.isCorrect());
    }

    public Answer toAnswer() {
        return new Answer(content, isCorrect);
    }

}
