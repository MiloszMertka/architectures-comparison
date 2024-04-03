package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.infrastructure.dto.UpdateQuizRequest;
import com.classic.elearning.course.service.QuizService;
import com.classic.elearning.shared.view.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

@Route(value = "courses/:courseName/quizzes/edit/:quizName", layout = MainLayout.class)
@PageTitle("Edit quiz")
@RolesAllowed("TEACHER")
public class EditQuizFormView extends VerticalLayout implements BeforeEnterObserver, QuizFormView {

    private final QuizService quizService;
    private final BeanValidationBinder<UpdateQuizRequest> binder = new BeanValidationBinder<>(UpdateQuizRequest.class);
    private final VerticalLayout fieldsContainer = new VerticalLayout();
    private final TextField name = new TextField("Name");
    private final DateTimePicker openingTime = new DateTimePicker("Opening time");
    private final DateTimePicker closingTime = new DateTimePicker("Closing time");
    private final Button addQuestionButton = new Button("Add question", new Icon(VaadinIcon.PLUS));
    private final Button saveButton = new Button("Save");
    private String courseName;
    private String quizName;

    public EditQuizFormView(QuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
        quizName = beforeEnterEvent.getRouteParameters().get("quizName").orElseThrow();
    }

    @Override
    public void removeQuestionField(QuestionComponent questionComponent) {
        binder.removeBinding(questionComponent.getContent());
        binder.removeBinding(questionComponent.getScore());
        fieldsContainer.remove(questionComponent);
        questionComponent.removeAllAnswerFields();
        binder.getBean().removeQuestion(questionComponent.getQuestionDto());
    }

    public void setQuiz(@NonNull UpdateQuizRequest updateQuizRequest) {
        binder.setBean(updateQuizRequest);
        bindFields();
        configureAddQuestionButton();
        configureSaveButton();
        configureLayout();
        createInitialQuestionFields();
    }

    private void bindFields() {
        binder.forField(name).bind(UpdateQuizRequest::getName, UpdateQuizRequest::setName);
        binder.forField(openingTime).bind(UpdateQuizRequest::getOpeningTime, UpdateQuizRequest::setOpeningTime);
        binder.forField(closingTime).bind(UpdateQuizRequest::getClosingTime, UpdateQuizRequest::setClosingTime);
    }

    private void configureAddQuestionButton() {
        addQuestionButton.addClickListener(event -> addQuestionField());
    }

    private void configureSaveButton() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> handleSaveQuizButtonClick(courseName, quizName, binder.getBean()));
    }

    private void configureLayout() {
        setWidthFull();
        fieldsContainer.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        fieldsContainer.setPadding(false);
        final var generalFields = new HorizontalLayout(openingTime, closingTime);
        openingTime.setWidthFull();
        closingTime.setWidthFull();
        fieldsContainer.add(name, generalFields);
        add(new H1("Edit quiz"), fieldsContainer, addQuestionButton, saveButton);
    }

    private void addQuestionField() {
        final var questionData = binder.getBean().addQuestion();
        final var questionComponent = new QuestionComponent(this, questionData, binder);
        fieldsContainer.add(questionComponent);
    }

    private void createInitialQuestionFields() {
        binder.getBean().getQuestionDtos().forEach(questionDto -> {
            final var questionComponent = new QuestionComponent(this, questionDto, binder);
            fieldsContainer.add(questionComponent);
        });
    }

    private void handleSaveQuizButtonClick(@NonNull String courseName, @NonNull String quizName, @NonNull UpdateQuizRequest updateQuizRequest) {
        if (!binder.validate().isOk()) {
            return;
        }

        try {
            quizService.updateQuiz(courseName, quizName, updateQuizRequest);
            navigateToCourseListView();
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void navigateToCourseListView() {
        getUI().ifPresent(ui -> ui.navigate(CourseListView.class));
    }

    private void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

}
