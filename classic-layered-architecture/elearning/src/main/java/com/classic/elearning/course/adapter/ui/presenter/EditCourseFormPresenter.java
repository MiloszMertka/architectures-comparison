package com.classic.elearning.course.adapter.ui.presenter;

import com.classic.elearning.course.adapter.dto.UpdateCourseRequest;
import com.classic.elearning.course.adapter.ui.EditCourseFormUI;
import com.classic.elearning.course.usecase.UpdateCourseUseCase;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.service.UserService;
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
    private final UserService userService;

    @Setter
    private EditCourseFormUI editCourseFormUI;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void handlePageLoad() {
        final var teachers = userService.browseUsers().stream()
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
