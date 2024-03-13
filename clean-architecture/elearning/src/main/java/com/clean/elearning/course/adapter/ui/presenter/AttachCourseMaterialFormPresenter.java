package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.AttachCourseMaterialRequest;
import com.clean.elearning.course.adapter.ui.AttachCourseMaterialFormUI;
import com.clean.elearning.course.usecase.AttachCourseMaterialUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AttachCourseMaterialFormPresenter {

    private final AttachCourseMaterialUseCase attachCourseMaterialUseCase;

    @Setter
    private AttachCourseMaterialFormUI attachCourseMaterialFormUI;

    public void handleFileRejected(@NonNull String reason) {
        attachCourseMaterialFormUI.showErrorMessage(reason);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void handleAttachCourseMaterialButtonClick(@NonNull String courseName, @NonNull AttachCourseMaterialRequest attachCourseMaterialRequest) {
        if (!attachCourseMaterialFormUI.isFormValid() || attachCourseMaterialRequest.getFileContent() == null) {
            return;
        }

        try {
            attachCourseMaterialUseCase.attachCourseMaterial(courseName, attachCourseMaterialRequest);
            attachCourseMaterialFormUI.navigateToCourseListView();
        } catch (Exception exception) {
            attachCourseMaterialFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
