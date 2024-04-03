package com.classic.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.dto.SolveQuizRequest;
import com.clean.elearning.course.adapter.ui.model.QuizViewModel;

public interface SolveQuizFormUI {

    void setQuiz(QuizViewModel quiz);

    void navigateToCourseListView();

    void showSaveQuizConfirmDialog(String courseName, SolveQuizRequest solveQuizRequest);

    void showErrorMessage(String message);

}
