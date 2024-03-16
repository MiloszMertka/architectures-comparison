package com.clean.elearning.course.adapter.ui.model;

import com.clean.elearning.course.domain.QuizResult;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import com.clean.elearning.user.domain.User;

public record QuizResultViewModel(
        UserViewModel student,
        String earnedScore
) {

    public static QuizResultViewModel fromStudentAndQuizResult(User student, QuizResult quizResult) {
        final var studentViewModel = UserViewModel.fromUser(student);
        final var earnedScore = String.format("%.2f", quizResult.getEarnedScore());
        return new QuizResultViewModel(studentViewModel, earnedScore);
    }

}
