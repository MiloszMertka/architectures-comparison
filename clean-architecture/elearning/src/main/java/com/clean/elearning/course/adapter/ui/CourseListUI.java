package com.clean.elearning.course.adapter.ui;

import com.clean.elearning.course.adapter.dto.RemoveCourseMaterialRequest;
import com.clean.elearning.course.adapter.dto.UpdateQuizRequest;
import com.clean.elearning.course.adapter.ui.model.CourseViewModel;
import com.clean.elearning.course.adapter.ui.model.QuizViewModel;

import java.util.List;

public interface CourseListUI {

    void showCourses(List<CourseViewModel> courses);

    void navigateToAttachCourseMaterialFormView(String courseName);

    void showRemoveCourseMaterialConfirmDialog(String courseName, RemoveCourseMaterialRequest removeCourseMaterialRequest);

    void navigateToCreateQuizFormView(String courseName);

    void navigateToQuizResultListView(String courseName, String quizName);

    void navigateToEditQuizFormView(String courseName, UpdateQuizRequest updateQuizRequest);

    void showDeleteQuizConfirmDialog(String courseName, String quizName);

    void showErrorMessage(String message);

    void navigateToSolveQuizFormView(String courseName, QuizViewModel quiz);

}
