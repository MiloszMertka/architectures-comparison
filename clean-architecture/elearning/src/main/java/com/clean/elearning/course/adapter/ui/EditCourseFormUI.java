package com.clean.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.dto.UpdateCourseRequest;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;

import java.util.List;

public interface EditCourseFormUI {

    void setCourse(UpdateCourseRequest course);

    void setTeachers(List<UserViewModel> teachers);

    boolean isFormValid();

    void navigateToAdminCourseListView();

    void showErrorMessage(String message);

}
