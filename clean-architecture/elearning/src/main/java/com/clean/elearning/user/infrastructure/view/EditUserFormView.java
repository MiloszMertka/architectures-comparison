package com.clean.elearning.user.infrastructure.view;

import com.clean.elearning.shared.view.MainLayout;
import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.adapter.ui.EditUserFormUI;
import com.clean.elearning.user.adapter.ui.presenter.EditUserFormPresenter;
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
import com.vaadin.flow.router.Route;
import org.springframework.lang.NonNull;

@Route(value = "users/edit", layout = MainLayout.class)
public class EditUserFormView extends FormLayout implements HasUrlParameter<String>, EditUserFormUI {

    private final EditUserFormPresenter editUserFormPresenter;
    private final BeanValidationBinder<UpdateUserRequest> binder = new BeanValidationBinder<>(UpdateUserRequest.class);
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("Email");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private String userEmail;

    public EditUserFormView(EditUserFormPresenter editUserFormPresenter) {
        this.editUserFormPresenter = editUserFormPresenter;
        editUserFormPresenter.setEditUserFormUI(this);
        binder.bindInstanceFields(this);
        final var content = createContent();
        add(content);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public void setUser(@NonNull UpdateUserRequest user) {
        binder.setBean(user);
    }

    @Override
    public boolean isFormValid() {
        return binder.isValid();
    }

    @Override
    public void navigateToUserListView() {
        getUI().ifPresent(ui -> ui.navigate(UserListView.class));
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    private Component createContent() {
        final var heading = new H1("Edit user");
        final var fieldsLayout = crateFieldsLayout();
        final var buttonsLayout = createButtonsLayout();
        return new VerticalLayout(heading, fieldsLayout, buttonsLayout);
    }

    private Component crateFieldsLayout() {
        return new FormLayout(firstName, lastName, email);
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> editUserFormPresenter.handleSaveUserButtonClick(userEmail, binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate("/users")));

        bindSaveButtonToFormValidation();

        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void bindSaveButtonToFormValidation() {
        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));
    }

}
