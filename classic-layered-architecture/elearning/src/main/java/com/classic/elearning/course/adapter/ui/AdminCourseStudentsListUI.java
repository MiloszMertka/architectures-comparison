package com.classic.elearning.course.adapter.ui;

import com.classic.elearning.course.adapter.dto.RemoveStudentsFromCourseRequest;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;

import java.util.List;

public interface AdminCourseStudentsListUI {

    void showCourseStudents(List<UserViewModel> students);

    void setStudentsAvailableToAssign(List<UserViewModel> students);

    void showRemoveStudentsFromCourseConfirmDialog(String courseName, RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest);

    void showErrorMessage(String message);

}
