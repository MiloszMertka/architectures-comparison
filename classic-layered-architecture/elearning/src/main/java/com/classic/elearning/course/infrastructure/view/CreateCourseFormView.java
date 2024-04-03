package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.adapter.dto.CreateCourseRequest;
import com.classic.elearning.course.adapter.ui.CreateCourseFormUI;
import com.classic.elearning.course.adapter.ui.presenter.CreateCourseFormPresenter;
import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.adapter.ui.model.UserViewModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "courses/create", layout = MainLayout.class)
@PageTitle("Create course")
@RolesAllowed("ADMIN")
public class CreateCourseFormView extends VerticalLayout implements CreateCourseFormUI {

    private final CreateCourseFormPresenter createCourseFormPresenter;
    private final BeanValidationBinder<CreateCourseRequest> binder = new BeanValidationBinder<>(CreateCourseRequest.class);
    private final TextField name = new TextField("Name");
    private final ComboBox<UserViewModel> teacher = new ComboBox<>("Teacher");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    public CreateCourseFormView(CreateCourseFormPresenter createCourseFormPresenter) {
        this.createCourseFormPresenter = createCourseFormPresenter;
        createCourseFormPresenter.setCreateCourseFormUI(this);
        binder.bindInstanceFields(this);
        binder.setBean(new CreateCourseRequest());
        final var content = createContent();
        add(content);
    }

    @PostConstruct
    private void init() {
        createCourseFormPresenter.handlePageLoad();
    }

    @Override
    public void setTeachers(@NonNull List<UserViewModel> teachers) {
        teacher.setItems(teachers);
    }

    @Override
    public boolean isFormValid() {
        return binder.validate().isOk();
    }

    @Override
    public void navigateToAdminCourseListView() {
        getUI().ifPresent(ui -> ui.navigate(AdminCourseListView.class));
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    private Component createContent() {
        final var heading = new H1("Create course");
        final var fieldsLayout = createFieldsLayout();
        final var buttonsLayout = createButtonsLayout();
        return new VerticalLayout(heading, fieldsLayout, buttonsLayout);
    }

    private Component createFieldsLayout() {
        configureTeacherComboBox();
        return new FormLayout(name, teacher);
    }

    private void configureTeacherComboBox() {
        teacher.setItemLabelGenerator(UserViewModel::fullName);
        teacher.setPlaceholder("Select teacher");
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> createCourseFormPresenter.handleSaveCourseButtonClick(binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(AdminCourseListView.class)));

        return new HorizontalLayout(saveButton, cancelButton);
    }

}
