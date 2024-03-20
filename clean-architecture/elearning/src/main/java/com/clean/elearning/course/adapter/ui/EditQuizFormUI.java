package com.clean.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.dto.UpdateQuizRequest;

public interface EditQuizFormUI {

    void setQuiz(UpdateQuizRequest updateQuizRequest);

    boolean isFormValid();

    void navigateToCourseListView();

    void showErrorMessage(String message);

}
