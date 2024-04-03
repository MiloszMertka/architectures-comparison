package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.adapter.dto.CreateQuizRequest;
import com.classic.elearning.course.adapter.ui.CreateQuizFormUI;
import com.classic.elearning.course.adapter.ui.presenter.CreateQuizFormPresenter;
import com.classic.elearning.course.infrastructure.view.CourseListView;
import com.classic.elearning.course.infrastructure.view.QuestionComponent;
import com.classic.elearning.course.infrastructure.view.QuizFormView;
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

@Route(value = "courses/:courseName/quizzes/create", layout = MainLayout.class)
@PageTitle("Create quiz")
@RolesAllowed("TEACHER")
public class CreateQuizFormView extends VerticalLayout implements BeforeEnterObserver, CreateQuizFormUI, QuizFormView {

    private final CreateQuizFormPresenter createQuizFormPresenter;
    private final BeanValidationBinder<CreateQuizRequest> binder = new BeanValidationBinder<>(CreateQuizRequest.class);
    private final VerticalLayout fieldsContainer = new VerticalLayout();
    private final TextField name = new TextField("Name");
    private final DateTimePicker openingTime = new DateTimePicker("Opening time");
    private final DateTimePicker closingTime = new DateTimePicker("Closing time");
    private final Button addQuestionButton = new Button("Add question", new Icon(VaadinIcon.PLUS));
    private final Button saveButton = new Button("Save");
    private String courseName;

    public CreateQuizFormView(CreateQuizFormPresenter createQuizFormPresenter) {
        this.createQuizFormPresenter = createQuizFormPresenter;
        createQuizFormPresenter.setCreateQuizFormUI(this);
        binder.setBean(new CreateQuizRequest());
        bindFields();
        configureAddQuestionButton();
        configureSaveButton();
        configureLayout();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
    }

    @Override
    public boolean isFormValid() {
        return binder.validate().isOk();
    }

    @Override
    public void navigateToCourseListView() {
        getUI().ifPresent(ui -> ui.navigate(CourseListView.class));
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    @Override
    public void removeQuestionField(com.classic.elearning.course.infrastructure.view.QuestionComponent questionComponent) {
        binder.removeBinding(questionComponent.getContent());
        binder.removeBinding(questionComponent.getScore());
        fieldsContainer.remove(questionComponent);
        questionComponent.removeAllAnswerFields();
        binder.getBean().removeQuestion(questionComponent.getQuestionDto());
    }

    private void bindFields() {
        binder.forField(name).bind(CreateQuizRequest::getName, CreateQuizRequest::setName);
        binder.forField(openingTime).bind(CreateQuizRequest::getOpeningTime, CreateQuizRequest::setOpeningTime);
        binder.forField(closingTime).bind(CreateQuizRequest::getClosingTime, CreateQuizRequest::setClosingTime);
    }

    private void configureAddQuestionButton() {
        addQuestionButton.addClickListener(event -> addQuestionField());
    }

    private void configureSaveButton() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> createQuizFormPresenter.handleSaveQuizButtonClick(courseName, binder.getBean()));
    }

    private void configureLayout() {
        setWidthFull();
        fieldsContainer.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        fieldsContainer.setPadding(false);
        final var generalFields = new HorizontalLayout(openingTime, closingTime);
        openingTime.setWidthFull();
        closingTime.setWidthFull();
        fieldsContainer.add(name, generalFields);
        addQuestionField();
        add(new H1("Create quiz"), fieldsContainer, addQuestionButton, saveButton);
    }

    private void addQuestionField() {
        final var questionData = binder.getBean().addQuestion();
        final var questionComponent = new QuestionComponent(this, questionData, binder);
        fieldsContainer.add(questionComponent);
    }

}
