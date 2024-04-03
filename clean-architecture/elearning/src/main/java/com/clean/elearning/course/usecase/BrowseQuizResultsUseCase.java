package com.clean.elearning.course.usecase;

import com.clean.elearning.course.domain.QuizResult;
import com.clean.elearning.user.domain.User;

import java.util.Map;

public interface BrowseQuizResultsUseCase {

    Map<User, QuizResult> browseQuizResults(String courseName, String quizName);

}
