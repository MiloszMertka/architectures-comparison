package com.clean.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.dto.UpdateCourseRequest;
import com.clean.elearning.course.adapter.ui.model.CourseViewModel;

import java.util.List;

public interface AdminCourseListUI {

    void showCourses(List<CourseViewModel> courses);

    void navigateToCreateCourseFormView();

    void navigateToEditCourseFormView(UpdateCourseRequest updateCourseRequest);

    void showDeleteCourseConfirmDialog(CourseViewModel course);

}
