package com.clean.elearning.course.usecase;

import com.clean.elearning.course.usecase.model.CreateQuizModel;

public interface CreateQuizUseCase {

    void createQuiz(String courseName, CreateQuizModel createQuizModel);

}
