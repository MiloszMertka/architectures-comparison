package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.SolveQuizRequest;
import com.clean.elearning.course.adapter.ui.SolveQuizFormUI;
import com.clean.elearning.course.usecase.SolveQuizUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SolveQuizFormPresenter {

    private final SolveQuizUseCase solveQuizUseCase;

    @Setter
    private SolveQuizFormUI solveQuizFormUI;

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void handleSolveQuizButtonClick(@NonNull String courseName, @NonNull SolveQuizRequest solveQuizRequest) {
        solveQuizFormUI.showSaveQuizConfirmDialog(courseName, solveQuizRequest);
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void handleSolveQuizConfirm(@NonNull String courseName, @NonNull SolveQuizRequest solveQuizRequest) {
        try {
            solveQuizUseCase.solveQuiz(courseName, solveQuizRequest);
            solveQuizFormUI.navigateToCourseListView();
        } catch (Exception exception) {
            solveQuizFormUI.showErrorMessage(exception.getMessage());
        }
    }

}
