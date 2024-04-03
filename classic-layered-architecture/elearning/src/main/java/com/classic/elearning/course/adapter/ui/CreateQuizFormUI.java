package com.classic.elearning.course.adapter.ui;

public interface CreateQuizFormUI {

    boolean isFormValid();

    void navigateToCourseListView();

    void showErrorMessage(String message);

}
