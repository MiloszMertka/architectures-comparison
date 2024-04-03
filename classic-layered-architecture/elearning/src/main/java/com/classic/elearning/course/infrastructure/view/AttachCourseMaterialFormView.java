package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.infrastructure.dto.AttachCourseMaterialRequest;
import com.classic.elearning.course.service.CourseMaterialService;
import com.classic.elearning.shared.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Route(value = "courses/:courseName/materials", layout = MainLayout.class)
@PageTitle("Attach course material")
@RolesAllowed("TEACHER")
public class AttachCourseMaterialFormView extends VerticalLayout implements BeforeEnterObserver {

    private final CourseMaterialService courseMaterialService;
    private final BeanValidationBinder<AttachCourseMaterialRequest> binder = new BeanValidationBinder<>(AttachCourseMaterialRequest.class);
    private final TextField name = new TextField("Name");
    private final MemoryBuffer receiver = new MemoryBuffer();
    private final Upload file = new Upload(receiver);
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private String courseName;

    public AttachCourseMaterialFormView(CourseMaterialService courseMaterialService) {
        this.courseMaterialService = courseMaterialService;
        binder.bindInstanceFields(this);
        binder.setBean(new AttachCourseMaterialRequest());
        final var content = createContent();
        add(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
    }

    private Component createContent() {
        final var heading = new H1("Attach course material");
        final var fieldsLayout = createFieldsLayout();
        final var buttonsLayout = createButtonsLayout();
        return new VerticalLayout(heading, fieldsLayout, buttonsLayout);
    }

    private Component createFieldsLayout() {
        configureFileUpload();
        return new FormLayout(name, file);
    }

    private void configureFileUpload() {
        file.setAcceptedFileTypes("application/pdf", "image/png", "image/jpeg", "text/plain");
        file.setMaxFiles(1);
        file.setMaxFileSize(50 * 1024 * 1024);
        file.addFileRejectedListener(event -> handleFileRejected(event.getErrorMessage()));
        file.addSucceededListener(event -> {
            try {
                final var fileContent = receiver.getInputStream().readAllBytes();
                binder.getBean().setFileContent(fileContent);
                binder.getBean().setFileExtension(receiver.getFileData().getFileName().split("\\.")[1]);
            } catch (IOException exception) {
                handleFileRejected("Failed to read file content");
            }
        });
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> handleAttachCourseMaterialButtonClick(courseName, binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(CourseListView.class)));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void handleFileRejected(@NonNull String reason) {
        showErrorMessage(reason);
    }

    private void handleAttachCourseMaterialButtonClick(@NonNull String courseName, @NonNull AttachCourseMaterialRequest attachCourseMaterialRequest) {
        if (!binder.validate().isOk() || attachCourseMaterialRequest.getFileContent() == null) {
            return;
        }

        try {
            courseMaterialService.attachCourseMaterial(courseName, attachCourseMaterialRequest);
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
