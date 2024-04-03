package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.adapter.dto.SolveQuizRequest;
import com.classic.elearning.course.adapter.ui.SolveQuizFormUI;
import com.classic.elearning.course.adapter.ui.model.AnswerViewModel;
import com.classic.elearning.course.adapter.ui.model.QuizViewModel;
import com.classic.elearning.course.adapter.ui.presenter.SolveQuizFormPresenter;
import com.classic.elearning.course.infrastructure.view.CourseListView;
import com.classic.elearning.shared.view.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "courses/:courseName/quizzes/:quizName/solve", layout = MainLayout.class)
@PageTitle("Solve quiz")
@RolesAllowed("STUDENT")
public class SolveQuizFormView extends VerticalLayout implements BeforeEnterObserver, SolveQuizFormUI {

    private final SolveQuizFormPresenter solveQuizFormPresenter;
    private final Map<String, RadioButtonGroup<String>> answeredQuestions = new HashMap<>();
    private final Button saveButton = new Button("Save");
    private QuizViewModel quiz;
    private String courseName;

    public SolveQuizFormView(SolveQuizFormPresenter solveQuizFormPresenter) {
        this.solveQuizFormPresenter = solveQuizFormPresenter;
        solveQuizFormPresenter.setSolveQuizFormUI(this);
    }

    @Override
    public void setQuiz(@NonNull QuizViewModel quiz) {
        this.quiz = quiz;
        createQuizTitle();
        createQuestionComponents();
        configureSaveButton();
    }

    @Override
    public void navigateToCourseListView() {
        getUI().ifPresent(ui -> ui.navigate(CourseListView.class));
    }

    @Override
    public void showSaveQuizConfirmDialog(@NonNull String courseName, @NonNull SolveQuizRequest finishQuizRequest) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Finish quiz");
        confirmDialog.setText("Are you sure you want to save and finish this quiz?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Save and finish");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.addConfirmListener(event -> solveQuizFormPresenter.handleSolveQuizConfirm(courseName, finishQuizRequest));
        confirmDialog.open();
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        beforeEnterEvent.getRouteParameters().get("courseName").ifPresent(courseName -> this.courseName = courseName);
    }

    private void createQuizTitle() {
        final var quizTitle = new H1(quiz.name());
        add(quizTitle);
    }

    private void createQuestionComponents() {
        quiz.questions().forEach(question -> {
            final var radioButtonGroup = new RadioButtonGroup<String>();
            radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
            radioButtonGroup.setLabel(question.content());
            radioButtonGroup.setItems(question.answers().stream()
                    .map(AnswerViewModel::content)
                    .toList());
            answeredQuestions.put(question.content(), radioButtonGroup);
            add(radioButtonGroup);
        });
    }

    private void configureSaveButton() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            final var finishQuizRequest = new SolveQuizRequest();
            finishQuizRequest.setQuizName(quiz.name());
            finishQuizRequest.setAnsweredQuestions(getAnswers());
            solveQuizFormPresenter.handleSolveQuizButtonClick(courseName, finishQuizRequest);
        });
        add(saveButton);
    }

    private Map<String, String> getAnswers() {
        return answeredQuestions.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
