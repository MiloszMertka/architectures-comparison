package com.classic.elearning.course.usecase;

import com.classic.elearning.course.usecase.model.UpdateQuizModel;

public interface UpdateQuizUseCase {

    void updateQuiz(String courseName, String quizName, UpdateQuizModel updateQuizModel);

}
