package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.AssignStudentsToCourseRequest;
import com.clean.elearning.course.adapter.dto.RemoveStudentsFromCourseRequest;
import com.clean.elearning.course.adapter.ui.AdminCourseStudentsListUI;
import com.clean.elearning.course.usecase.AssignStudentsToCourseUseCase;
import com.clean.elearning.course.usecase.RemoveStudentsFromCourseUseCase;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
import com.clean.elearning.course.usecase.ViewCourseUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AdminCourseStudentsListPresenter {

    private final AssignStudentsToCourseUseCase assignStudentsToCourseUseCase;
    private final RemoveStudentsFromCourseUseCase removeStudentsFromCourseUseCase;
    private final BrowseUsersUseCase browseUsersUseCase;
    private final ViewCourseUseCase viewCourseUseCase;

    @Setter
    private AdminCourseStudentsListUI adminCourseStudentsListUI;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public void handlePageLoad(@NonNull String courseName) {
        final var course = viewCourseUseCase.viewCourse(courseName);
        final var courseStudents = course.getStudents().stream()
                .map(UserViewModel::fromUser)
                .toList();
        final var studentsAvailableToAssign = browseUsersUseCase.browseUsers().stream()
                .filter(User::isStudent)
                .filter(user -> !course.getStudents().contains(user))
                .map(UserViewModel::fromUser)
                .toList();
        adminCourseStudentsListUI.showCourseStudents(courseStudents);
        adminCourseStudentsListUI.setStudentsAvailableToAssign(studentsAvailableToAssign);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleAssignStudentsButtonClick(@NonNull String courseName, @NonNull AssignStudentsToCourseRequest assignStudentsToCourseRequest) {
        try {
            assignStudentsToCourseUseCase.assignStudentsToCourse(courseName, assignStudentsToCourseRequest);
            handlePageLoad(courseName);
        } catch (Exception exception) {
            adminCourseStudentsListUI.showErrorMessage(exception.getMessage());
        }
    }

    public void handleRemoveStudentsButtonClick(@NonNull String courseName, @NonNull RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        adminCourseStudentsListUI.showRemoveStudentsFromCourseConfirmDialog(courseName, removeStudentsFromCourseRequest);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleRemoveStudentsFromCourseConfirm(@NonNull String courseName, @NonNull RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        try {
            removeStudentsFromCourseUseCase.removeStudentsFromCourse(courseName, removeStudentsFromCourseRequest);
            handlePageLoad(courseName);
        } catch (Exception exception) {
            adminCourseStudentsListUI.showErrorMessage(exception.getMessage());
        }
    }

}
