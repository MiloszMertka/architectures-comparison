package com.classic.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.UpdateQuizRequest;
import com.clean.elearning.course.adapter.ui.EditQuizFormUI;
import com.clean.elearning.course.usecase.UpdateQuizUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EditQuizFormPresenter {

    private final UpdateQuizUseCase updateQuizUseCase;

    @Setter
    private EditQuizFormUI editQuizFormUI;

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void handleSaveQuizButtonClick(@NonNull String courseName, @NonNull String quizName, @NonNull UpdateQuizRequest updateQuizRequest) {
        if (!editQuizFormUI.isFormValid()) {
            return;
        }

        try {
            updateQuizUseCase.updateQuiz(courseName, quizName, updateQuizRequest);
            editQuizFormUI.navigateToCourseListView();
        } catch (Exception exception) {
            editQuizFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
