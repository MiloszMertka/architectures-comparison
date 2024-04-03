package com.classic.elearning.course.usecase;

import com.classic.elearning.course.usecase.model.CreateQuizModel;

public interface CreateQuizUseCase {

    void createQuiz(String courseName, CreateQuizModel createQuizModel);

}
