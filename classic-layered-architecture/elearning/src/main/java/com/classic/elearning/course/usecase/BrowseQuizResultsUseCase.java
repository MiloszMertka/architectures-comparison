package com.classic.elearning.course.usecase;

import com.classic.elearning.course.domain.QuizResult;
import com.classic.elearning.user.domain.User;

import java.util.Map;

public interface BrowseQuizResultsUseCase {

    Map<User, QuizResult> browseQuizResults(String courseName, String quizName);

}
