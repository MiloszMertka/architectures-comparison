package com.clean.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.ui.model.CourseViewModel;

import java.util.List;

public interface CourseListUI {

    void showCourses(List<CourseViewModel> courses);

    void navigateToAttachCourseMaterialFormView(String courseName);

}
