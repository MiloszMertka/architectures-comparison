package com.clean.elearning.course.usecase;

import com.clean.elearning.course.usecase.model.UpdateQuizModel;

public interface UpdateQuizUseCase {

    void updateQuiz(String courseName, String quizName, UpdateQuizModel updateQuizModel);

}
