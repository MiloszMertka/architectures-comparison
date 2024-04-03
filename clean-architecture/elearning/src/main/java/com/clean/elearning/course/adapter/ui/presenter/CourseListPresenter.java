package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.AnswerDto;
import com.clean.elearning.course.adapter.dto.QuestionDto;
import com.clean.elearning.course.adapter.dto.RemoveCourseMaterialRequest;
import com.clean.elearning.course.adapter.dto.UpdateQuizRequest;
import com.clean.elearning.course.adapter.ui.CourseListUI;
import com.clean.elearning.course.adapter.ui.model.AnswerViewModel;
import com.clean.elearning.course.adapter.ui.model.CourseViewModel;
import com.clean.elearning.course.adapter.ui.model.QuestionViewModel;
import com.clean.elearning.course.adapter.ui.model.QuizViewModel;
import com.clean.elearning.course.usecase.BrowseUserCoursesUseCase;
import com.clean.elearning.course.usecase.DeleteQuizUseCase;
import com.clean.elearning.course.usecase.RemoveCourseMaterialUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CourseListPresenter {

    private final BrowseUserCoursesUseCase browseUserCoursesUseCase;
    private final RemoveCourseMaterialUseCase removeCourseMaterialUseCase;
    private final DeleteQuizUseCase deleteQuizUseCase;

    @Setter
    private CourseListUI courseListUI;

    @Transactional(readOnly = true)
    public void handlePageLoad() {
        final var courses = browseUserCoursesUseCase.browseUserCourses();
        final var courseViewModels = courses.stream()
                .map(CourseViewModel::fromCourse)
                .toList();
        courseListUI.showCourses(courseViewModels);
    }

    public void handleAttachCourseMaterialButtonClick(@NonNull String courseName) {
        courseListUI.navigateToAttachCourseMaterialFormView(courseName);
    }

    public void handleRemoveCourseMaterialButtonClick(@NonNull String courseName, @NonNull RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        courseListUI.showRemoveCourseMaterialConfirmDialog(courseName, removeCourseMaterialRequest);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void handleRemoveCourseMaterialConfirm(@NonNull String courseName, @NonNull RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        try {
            removeCourseMaterialUseCase.removeCourseMaterial(courseName, removeCourseMaterialRequest);
            handlePageLoad();
        } catch (Exception exception) {
            courseListUI.showErrorMessage(exception.getMessage());
        }
    }

    public void handleCreateQuizButtonClick(@NonNull String courseName) {
        courseListUI.navigateToCreateQuizFormView(courseName);
    }

    public void handleShowQuizResultsButtonClick(@NonNull String courseName, @NonNull String quizName) {
        courseListUI.navigateToQuizResultListView(courseName, quizName);
    }

    public void handleEditQuizButtonClick(@NonNull String courseName, @NonNull QuizViewModel quiz) {
        final var updateQuizRequest = createUpdateQuizRequest(quiz);
        courseListUI.navigateToEditQuizFormView(courseName, updateQuizRequest);
    }

    public void handleDeleteQuizButtonClick(@NonNull String courseName, @NonNull String quizName) {
        courseListUI.showDeleteQuizConfirmDialog(courseName, quizName);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void handleDeleteQuizConfirm(@NonNull String courseName, @NonNull String quizName) {
        try {
            deleteQuizUseCase.deleteQuiz(courseName, quizName);
            handlePageLoad();
        } catch (Exception exception) {
            courseListUI.showErrorMessage(exception.getMessage());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void handleSolveQuizButtonClick(@NonNull String courseName, @NonNull QuizViewModel quiz) {
        try {
            courseListUI.navigateToSolveQuizFormView(courseName, quiz);
        } catch (Exception exception) {
            courseListUI.showErrorMessage(exception.getMessage());
        }
    }

    private UpdateQuizRequest createUpdateQuizRequest(QuizViewModel quiz) {
        final var updateQuizRequest = new UpdateQuizRequest();
        updateQuizRequest.setName(quiz.name());
        updateQuizRequest.setOpeningTime(quiz.openingTime());
        updateQuizRequest.setClosingTime(quiz.closingTime());
        updateQuizRequest.setQuestions(quiz.questions().stream()
                .map(this::mapQuestionViewModelToDto)
                .collect(Collectors.toList()));
        return updateQuizRequest;
    }

    private QuestionDto mapQuestionViewModelToDto(QuestionViewModel questionViewModel) {
        final var questionDto = new QuestionDto();
        questionDto.setContent(questionViewModel.content());
        questionDto.setScore(questionViewModel.score());
        questionDto.setAnswers(questionViewModel.answers().stream()
                .map(this::mapAnswerViewModelToDto)
                .collect(Collectors.toList()));
        return questionDto;
    }

    private AnswerDto mapAnswerViewModelToDto(AnswerViewModel answerViewModel) {
        final var answerDto = new AnswerDto();
        answerDto.setContent(answerViewModel.content());
        answerDto.setCorrect(answerViewModel.isCorrect());
        return answerDto;
    }

}
