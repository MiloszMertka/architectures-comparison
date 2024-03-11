package com.clean.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.dto.RemoveStudentsFromCourseRequest;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;

import java.util.List;

public interface AdminCourseStudentsListUI {

    void showCourseStudents(List<UserViewModel> students);

    void setStudentsAvailableToAssign(List<UserViewModel> students);

    void showRemoveStudentsFromCourseConfirmDialog(String courseName, RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest);

    void showErrorMessage(String message);

}
