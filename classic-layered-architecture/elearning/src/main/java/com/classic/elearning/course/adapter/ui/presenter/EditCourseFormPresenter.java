package com.classic.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.UpdateCourseRequest;
import com.clean.elearning.course.adapter.ui.EditCourseFormUI;
import com.clean.elearning.course.usecase.UpdateCourseUseCase;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.usecase.BrowseUsersUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EditCourseFormPresenter {

    private final UpdateCourseUseCase updateCourseUseCase;
    private final BrowseUsersUseCase browseUsersUseCase;

    @Setter
    private EditCourseFormUI editCourseFormUI;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void handlePageLoad() {
        final var teachers = browseUsersUseCase.browseUsers().stream()
                .filter(User::isTeacher)
                .map(UserViewModel::fromUser)
                .toList();
        editCourseFormUI.setTeachers(teachers);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSaveCourseButtonClick(@NonNull String courseName, @NonNull UpdateCourseRequest updateCourseRequest) {
        if (!editCourseFormUI.isFormValid()) {
            return;
        }

        try {
            updateCourseUseCase.updateCourse(courseName, updateCourseRequest);
            editCourseFormUI.navigateToAdminCourseListView();
        } catch (Exception exception) {
            editCourseFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
