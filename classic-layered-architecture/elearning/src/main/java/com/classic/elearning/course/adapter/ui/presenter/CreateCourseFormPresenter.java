package com.classic.elearning.course.adapter.ui.presenter;

import com.classic.elearning.course.adapter.dto.CreateCourseRequest;
import com.classic.elearning.course.adapter.ui.CreateCourseFormUI;
import com.classic.elearning.course.usecase.CreateCourseUseCase;
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
public class CreateCourseFormPresenter {

    private final CreateCourseUseCase createCourseUseCase;
    private final UserService userService;

    @Setter
    private CreateCourseFormUI createCourseFormUI;

    @Transactional(readOnly = true)
    public void handlePageLoad() {
        final var teachers = userService.browseUsers().stream()
                .filter(User::isTeacher)
                .map(UserViewModel::fromUser)
                .toList();
        createCourseFormUI.setTeachers(teachers);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void handleSaveCourseButtonClick(@NonNull CreateCourseRequest createCourseRequest) {
        if (!createCourseFormUI.isFormValid()) {
            return;
        }

        try {
            createCourseUseCase.createCourse(createCourseRequest);
            createCourseFormUI.navigateToAdminCourseListView();
        } catch (Exception exception) {
            createCourseFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
