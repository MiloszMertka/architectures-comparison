package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.domain.Answer;
import com.classic.elearning.course.domain.Quiz;
import com.classic.elearning.course.infrastructure.dto.SolveQuizRequest;
import com.classic.elearning.course.service.QuizService;
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
public class SolveQuizFormView extends VerticalLayout implements BeforeEnterObserver {

    private final QuizService quizService;
    private final Map<String, RadioButtonGroup<String>> answeredQuestions = new HashMap<>();
    private final Button saveButton = new Button("Save");
    private Quiz quiz;
    private String courseName;

    public SolveQuizFormView(QuizService quizService) {
        this.quizService = quizService;
    }

    public void setQuiz(@NonNull Quiz quiz) {
        this.quiz = quiz;
        createQuizTitle();
        createQuestionComponents();
        configureSaveButton();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        beforeEnterEvent.getRouteParameters().get("courseName").ifPresent(courseName -> this.courseName = courseName);
    }

    private void createQuizTitle() {
        final var quizTitle = new H1(quiz.getName());
        add(quizTitle);
    }

    private void createQuestionComponents() {
        quiz.getQuestions().forEach(question -> {
            final var radioButtonGroup = new RadioButtonGroup<String>();
            radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
            radioButtonGroup.setLabel(question.getContent());
            radioButtonGroup.setItems(question.getAnswers().stream()
                    .map(Answer::getContent)
                    .toList());
            answeredQuestions.put(question.getContent(), radioButtonGroup);
            add(radioButtonGroup);
        });
    }

    private void configureSaveButton() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            final var finishQuizRequest = new SolveQuizRequest();
            finishQuizRequest.setQuizName(quiz.getName());
            finishQuizRequest.setAnsweredQuestions(getAnswers());
            handleSolveQuizButtonClick(courseName, finishQuizRequest);
        });
        add(saveButton);
    }

    private Map<String, String> getAnswers() {
        return answeredQuestions.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void handleSolveQuizButtonClick(@NonNull String courseName, @NonNull SolveQuizRequest solveQuizRequest) {
        showSaveQuizConfirmDialog(courseName, solveQuizRequest);
    }

    private void handleSolveQuizConfirm(@NonNull String courseName, @NonNull SolveQuizRequest solveQuizRequest) {
        try {
            quizService.solveQuiz(courseName, solveQuizRequest);
            navigateToCourseListView();
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void navigateToCourseListView() {
        getUI().ifPresent(ui -> ui.navigate(CourseListView.class));
    }

    public void showSaveQuizConfirmDialog(@NonNull String courseName, @NonNull SolveQuizRequest finishQuizRequest) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Finish quiz");
        confirmDialog.setText("Are you sure you want to save and finish this quiz?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Save and finish");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName());
        confirmDialog.addConfirmListener(event -> handleSolveQuizConfirm(courseName, finishQuizRequest));
        confirmDialog.open();
    }

    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

}
