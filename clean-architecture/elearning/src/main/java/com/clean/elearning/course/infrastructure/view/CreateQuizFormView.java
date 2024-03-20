package com.clean.elearning.course.infrastructure.view;

import com.clean.elearning.course.adapter.dto.CreateQuizRequest;
import com.clean.elearning.course.adapter.ui.CreateQuizFormUI;
import com.clean.elearning.course.adapter.ui.presenter.CreateQuizFormPresenter;
import com.clean.elearning.shared.view.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Route(value = "courses/:courseName/quizzes/create", layout = MainLayout.class)
@PageTitle("Create quiz")
@RolesAllowed("TEACHER")
public class CreateQuizFormView extends VerticalLayout implements BeforeEnterObserver, CreateQuizFormUI {

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

    public void removeQuestionField(QuestionComponent questionComponent) {
        binder.removeBinding(questionComponent.getContent());
        binder.removeBinding(questionComponent.getScore());
        fieldsContainer.remove(questionComponent);
        questionComponent.removeAllAnswerFields();
        binder.getBean().removeQuestion(questionComponent.getQuestionData());
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

    @Getter
    public static class QuestionComponent extends VerticalLayout {

        private final VerticalLayout fieldsContainer = new VerticalLayout();
        private final TextField content = new TextField("Content");
        private final NumberField score = new NumberField("Score");
        private final Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
        private final List<AnswerComponent> answers = new ArrayList<>();
        private final Button addAnswerButton = new Button("Add answer", new Icon(VaadinIcon.PLUS));
        private final CreateQuizFormView createQuizFormView;
        private final CreateQuizRequest.QuestionData questionData;
        private final Binder<CreateQuizRequest> binder;

        public QuestionComponent(CreateQuizFormView createQuizFormView, CreateQuizRequest.QuestionData questionData, Binder<CreateQuizRequest> binder) {
            this.createQuizFormView = createQuizFormView;
            this.questionData = questionData;
            this.binder = binder;
            bindFields();
            configureAddAnswerButton();
            configureRemoveButton();
            configureLayout();
        }

        public void addAnswerField() {
            final var answerData = questionData.addAnswer();
            final var answerComponent = new AnswerComponent(this, answerData, binder);
            answers.add(answerComponent);
            fieldsContainer.add(answerComponent);
        }

        public void removeAllAnswerFields() {
            answers.forEach((answerComponent) -> {
                binder.removeBinding(answerComponent.getContent());
                binder.removeBinding(answerComponent.getIsCorrect());
                fieldsContainer.remove(answerComponent);
                questionData.removeAnswer(answerComponent.getAnswerData());
            });
            answers.clear();
        }

        public void removeAnswerField(AnswerComponent answerComponent) {
            binder.removeBinding(answerComponent.getContent());
            binder.removeBinding(answerComponent.getIsCorrect());
            fieldsContainer.remove(answerComponent);
            questionData.removeAnswer(answerComponent.getAnswerData());
            answers.remove(answerComponent);
        }

        private void bindFields() {
            binder.forField(content).bind((createQuizRequest) -> questionData.getContent(), (createQuizRequest, value) -> questionData.setContent(value));
            binder.forField(score).bind((createQuizRequest) -> questionData.getScore(), (createQuizRequest, value) -> questionData.setScore(value));
        }

        private void configureAddAnswerButton() {
            addAnswerButton.addClickListener(event -> addAnswerField());
        }

        private void configureRemoveButton() {
            removeButton.addClickListener(event -> createQuizFormView.removeQuestionField(this));
        }

        private void configureLayout() {
            setWidthFull();
            setPadding(false);
            add(new Hr());
            fieldsContainer.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
            fieldsContainer.setPadding(false);
            removeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            final var generalFields = new HorizontalLayout(removeButton, content, score);
            generalFields.setDefaultVerticalComponentAlignment(Alignment.END);
            content.setWidthFull();
            score.setWidthFull();
            fieldsContainer.add(generalFields);
            addAnswerField();
            add(fieldsContainer, addAnswerButton);
        }

    }

    @Getter
    public static class AnswerComponent extends VerticalLayout {

        private final TextField content = new TextField("Content");
        private final Checkbox isCorrect = new Checkbox("Is correct");
        private final Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
        private final QuestionComponent questionComponent;
        private final CreateQuizRequest.AnswerData answerData;
        private final Binder<CreateQuizRequest> binder;

        public AnswerComponent(QuestionComponent questionComponent, CreateQuizRequest.AnswerData answerData, Binder<CreateQuizRequest> binder) {
            this.questionComponent = questionComponent;
            this.answerData = answerData;
            this.binder = binder;
            bindFields();
            configureRemoveButton();
            configureLayout();
        }

        private void bindFields() {
            binder.forField(content).bind((createQuizRequest) -> answerData.getContent(), (createQuizRequest, value) -> answerData.setContent(value));
            binder.forField(isCorrect).bind((createQuizRequest) -> answerData.isCorrect(), (createQuizRequest, value) -> answerData.setCorrect(value));
        }

        private void configureRemoveButton() {
            removeButton.addClickListener(event -> questionComponent.removeAnswerField(this));
        }

        private void configureLayout() {
            setWidthFull();
            setPadding(false);
            removeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            final var fieldsContainer = new HorizontalLayout(removeButton, content);
            content.setWidthFull();
            fieldsContainer.setDefaultVerticalComponentAlignment(Alignment.END);
            fieldsContainer.setWidthFull();
            add(fieldsContainer, isCorrect);
        }

    }

}
