package com.classic.elearning.course.adapter.ui;

import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;

import java.util.List;

public interface CreateCourseFormUI {

    void setTeachers(List<UserViewModel> teachers);

    boolean isFormValid();

    void navigateToAdminCourseListView();

    void showErrorMessage(String message);

}
