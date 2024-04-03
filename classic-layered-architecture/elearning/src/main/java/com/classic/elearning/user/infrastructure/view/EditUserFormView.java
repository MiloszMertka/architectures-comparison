package com.classic.elearning.user.infrastructure.view;

import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.service.UserService;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

@Route(value = "users/edit", layout = MainLayout.class)
@PageTitle("Edit user")
@RolesAllowed("ADMIN")
public class EditUserFormView extends VerticalLayout implements HasUrlParameter<String> {

    private final UserService userService;
    private final BeanValidationBinder<UpdateUserRequest> binder = new BeanValidationBinder<>(UpdateUserRequest.class);
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("Email");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private String userEmail;

    public EditUserFormView(UserService userService) {
        this.userService = userService;
        binder.bindInstanceFields(this);
        final var content = createContent();
        add(content);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUser(@NonNull UpdateUserRequest user) {
        binder.setBean(user);
    }

    private void navigateToUserListView() {
        getUI().ifPresent(ui -> ui.navigate(UserListView.class));
    }

    private void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    private Component createContent() {
        final var heading = new H1("Edit user");
        final var fieldsLayout = createFieldsLayout();
        final var buttonsLayout = createButtonsLayout();
        return new VerticalLayout(heading, fieldsLayout, buttonsLayout);
    }

    private Component createFieldsLayout() {
        return new FormLayout(firstName, lastName, email);
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> handleSaveUserButtonClick(userEmail, binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(UserListView.class)));

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void handleSaveUserButtonClick(@NonNull String userEmail, @NonNull UpdateUserRequest updateUserRequest) {
        if (!binder.validate().isOk()) {
            return;
        }

        try {
            userService.updateUser(userEmail, updateUserRequest);
            navigateToUserListView();
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

}
