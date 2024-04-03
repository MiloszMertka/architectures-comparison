package com.classic.elearning.course.adapter.database;

import com.classic.elearning.course.domain.QuizResult;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizResultSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double earnedScore;

    public static QuizResultSchema fromQuizResult(@NonNull QuizResult quizResult) {
        return new QuizResultSchema(null, quizResult.getEarnedScore());
    }

    public QuizResult toQuizResult() {
        return new QuizResult(earnedScore);
    }

}
