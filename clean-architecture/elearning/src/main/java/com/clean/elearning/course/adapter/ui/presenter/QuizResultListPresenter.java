package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.ui.QuizResultListUI;
import com.clean.elearning.course.adapter.ui.model.QuizResultViewModel;
import com.clean.elearning.course.usecase.BrowseQuizResultsUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class QuizResultListPresenter {

    private final BrowseQuizResultsUseCase browseQuizResultsUseCase;

    @Setter
    private QuizResultListUI quizResultListUI;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('TEACHER')")
    public void handlePageLoad(@NonNull String courseName, @NonNull String quizName) {
        final var quizResults = browseQuizResultsUseCase.browseQuizResults(courseName, quizName);
        final var quizResultsViewModels = quizResults.entrySet().stream()
                .map(entry -> QuizResultViewModel.fromStudentAndQuizResult(entry.getKey(), entry.getValue()))
                .toList();
        quizResultListUI.showQuizResults(quizResultsViewModels);
    }

}
