package com.classic.elearning.course.domain;

import com.classic.elearning.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant openingTime;

    @Column(nullable = false)
    private Instant closingTime;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Map<User, QuizResult> quizResults = new HashMap<>();

    public double getTotalScore() {
        return questions.stream()
                .mapToDouble(Question::getScore)
                .sum();
    }

    public void saveResults(User student, Map<Question, Answer> solvedQuestions) {
        validateStudentCanSaveResults(student);
        final var quizResult = new QuizResult(solvedQuestions);
        quizResults.put(student, quizResult);
    }

    private void validateStudentCanSaveResults(User student) {
        if (quizResults.containsKey(student)) {
            throw new IllegalStateException("Student already solved the quiz");
        }
    }

}
