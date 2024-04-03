package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.infrastructure.dto.AnswerDto;
import com.classic.elearning.course.infrastructure.dto.QuizRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

@Getter
public class AnswerComponent extends VerticalLayout {

    private final TextField content = new TextField("Content");
    private final Checkbox isCorrect = new Checkbox("Is correct");
    private final Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
    private final QuestionComponent questionComponent;
    private final AnswerDto answerDto;
    private final Binder<? extends QuizRequest> binder;

    public AnswerComponent(QuestionComponent questionComponent, AnswerDto answerDto, Binder<? extends QuizRequest> binder) {
        this.questionComponent = questionComponent;
        this.answerDto = answerDto;
        this.binder = binder;
        bindFields();
        configureRemoveButton();
        configureLayout();
    }

    private void bindFields() {
        binder.forField(content).bind((quizRequest) -> answerDto.getContent(), (quizRequest, value) -> answerDto.setContent(value));
        binder.forField(isCorrect).bind((quizRequest) -> answerDto.isCorrect(), (quizRequest, value) -> answerDto.setCorrect(value));
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
