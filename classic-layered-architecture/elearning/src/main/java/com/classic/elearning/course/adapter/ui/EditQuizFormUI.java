package com.classic.elearning.course.adapter.ui;

import com.classic.elearning.course.adapter.dto.UpdateQuizRequest;

public interface EditQuizFormUI {

    void setQuiz(UpdateQuizRequest updateQuizRequest);

    boolean isFormValid();

    void navigateToCourseListView();

    void showErrorMessage(String message);

}
