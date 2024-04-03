package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.infrastructure.dto.CreateCourseRequest;
import com.classic.elearning.course.service.CourseService;
import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.service.UserService;
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

@Route(value = "courses/create", layout = MainLayout.class)
@PageTitle("Create course")
@RolesAllowed("ADMIN")
public class CreateCourseFormView extends VerticalLayout {

    private final CourseService courseService;
    private final UserService userService;
    private final BeanValidationBinder<CreateCourseRequest> binder = new BeanValidationBinder<>(CreateCourseRequest.class);
    private final TextField name = new TextField("Name");
    private final ComboBox<User> teacher = new ComboBox<>("Teacher");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    public CreateCourseFormView(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
        binder.bindInstanceFields(this);
        binder.setBean(new CreateCourseRequest());
        final var content = createContent();
        add(content);
    }

    @PostConstruct
    private void init() {
        handlePageLoad();
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
        teacher.setItemLabelGenerator(User::getFullName);
        teacher.setPlaceholder("Select teacher");
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> handleSaveCourseButtonClick(binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(AdminCourseListView.class)));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void handlePageLoad() {
        final var teachers = userService.browseUsers().stream()
                .filter(User::isTeacher)
                .toList();
        teacher.setItems(teachers);
    }

    private void handleSaveCourseButtonClick(@NonNull CreateCourseRequest createCourseRequest) {
        if (!binder.validate().isOk()) {
            return;
        }

        try {
            courseService.createCourse(createCourseRequest);
            navigateToAdminCourseListView();
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void navigateToAdminCourseListView() {
        getUI().ifPresent(ui -> ui.navigate(AdminCourseListView.class));
    }

    private void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

}
