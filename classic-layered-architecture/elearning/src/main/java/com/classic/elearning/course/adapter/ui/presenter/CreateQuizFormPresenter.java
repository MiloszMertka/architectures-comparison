package com.classic.elearning.course.adapter.ui.presenter;

import com.classic.elearning.course.adapter.dto.CreateQuizRequest;
import com.classic.elearning.course.adapter.ui.CreateQuizFormUI;
import com.classic.elearning.course.usecase.CreateQuizUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateQuizFormPresenter {

    private final CreateQuizUseCase createQuizUseCase;

    @Setter
    private CreateQuizFormUI createQuizFormUI;

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void handleSaveQuizButtonClick(@NonNull String courseName, @NonNull CreateQuizRequest createQuizRequest) {
        if (!createQuizFormUI.isFormValid()) {
            return;
        }

        try {
            createQuizUseCase.createQuiz(courseName, createQuizRequest);
            createQuizFormUI.navigateToCourseListView();
        } catch (Exception exception) {
            createQuizFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
