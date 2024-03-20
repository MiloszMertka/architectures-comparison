package com.clean.elearning.course.infrastructure.view;

import com.clean.elearning.course.adapter.dto.QuestionDto;
import com.clean.elearning.course.adapter.dto.QuizRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionComponent extends VerticalLayout {

    private final VerticalLayout fieldsContainer = new VerticalLayout();
    private final TextField content = new TextField("Content");
    private final NumberField score = new NumberField("Score");
    private final Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
    private final List<AnswerComponent> answers = new ArrayList<>();
    private final Button addAnswerButton = new Button("Add answer", new Icon(VaadinIcon.PLUS));
    private final QuizFormView quizFormView;
    private final QuestionDto questionDto;
    private final Binder<? extends QuizRequest> binder;

    public QuestionComponent(QuizFormView quizFormView, QuestionDto questionDto, Binder<? extends QuizRequest> binder) {
        this.quizFormView = quizFormView;
        this.questionDto = questionDto;
        this.binder = binder;
        bindFields();
        configureAddAnswerButton();
        configureRemoveButton();
        configureLayout();
        createInitialAnswerFields();
    }

    public void addAnswerField() {
        final var answerDto = questionDto.addAnswer();
        final var answerComponent = new AnswerComponent(this, answerDto, binder);
        answers.add(answerComponent);
        fieldsContainer.add(answerComponent);
    }

    private void createInitialAnswerFields() {
        questionDto.getAnswers().forEach(answerDto -> {
            final var answerComponent = new AnswerComponent(this, answerDto, binder);
            answers.add(answerComponent);
            fieldsContainer.add(answerComponent);
        });
    }

    public void removeAllAnswerFields() {
        answers.forEach((answerComponent) -> {
            binder.removeBinding(answerComponent.getContent());
            binder.removeBinding(answerComponent.getIsCorrect());
            fieldsContainer.remove(answerComponent);
            questionDto.removeAnswer(answerComponent.getAnswerDto());
        });
        answers.clear();
    }

    public void removeAnswerField(@NonNull AnswerComponent answerComponent) {
        binder.removeBinding(answerComponent.getContent());
        binder.removeBinding(answerComponent.getIsCorrect());
        fieldsContainer.remove(answerComponent);
        questionDto.removeAnswer(answerComponent.getAnswerDto());
        answers.remove(answerComponent);
    }

    private void bindFields() {
        binder.forField(content).bind((quizRequest) -> questionDto.getContent(), (quizRequest, value) -> questionDto.setContent(value));
        binder.forField(score).bind((quizRequest) -> questionDto.getScore(), (quizRequest, value) -> questionDto.setScore(value));
    }

    private void configureAddAnswerButton() {
        addAnswerButton.addClickListener(event -> addAnswerField());
    }

    private void configureRemoveButton() {
        removeButton.addClickListener(event -> quizFormView.removeQuestionField(this));
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
        add(fieldsContainer, addAnswerButton);
    }

}
