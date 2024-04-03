package com.classic.elearning.user.infrastructure.view;

import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.adapter.dto.CreateUserRequest;
import com.classic.elearning.user.adapter.ui.CreateUserFormUI;
import com.classic.elearning.user.adapter.ui.presenter.CreateUserFormPresenter;
import com.classic.elearning.user.domain.Role;
import com.classic.elearning.user.infrastructure.view.UserListView;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

@Route(value = "users/create", layout = MainLayout.class)
@PageTitle("Create user")
@RolesAllowed("ADMIN")
public class CreateUserFormView extends VerticalLayout implements CreateUserFormUI {

    private final CreateUserFormPresenter createUserFormPresenter;
    private final BeanValidationBinder<CreateUserRequest> binder = new BeanValidationBinder<>(CreateUserRequest.class);
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("Email");
    private final PasswordField password = new PasswordField("Password");
    private final ComboBox<Role> role = new ComboBox<>("Role");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    public CreateUserFormView(CreateUserFormPresenter createUserFormPresenter) {
        this.createUserFormPresenter = createUserFormPresenter;
        createUserFormPresenter.setCreateUserFormUI(this);
        binder.bindInstanceFields(this);
        binder.setBean(new CreateUserRequest());
        final var content = createContent();
        add(content);
    }

    @Override
    public boolean isFormValid() {
        return binder.validate().isOk();
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
        final var heading = new H1("Create user");
        final var fieldsLayout = createFieldsLayout();
        final var buttonsLayout = createButtonsLayout();
        return new VerticalLayout(heading, fieldsLayout, buttonsLayout);
    }

    private Component createFieldsLayout() {
        configureRoleComboBox();
        return new FormLayout(firstName, lastName, email, password, role);
    }

    private void configureRoleComboBox() {
        role.setItems(Role.values());
        role.setItemLabelGenerator(role -> role.name().toLowerCase());
        role.setPlaceholder("Select role");
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> createUserFormPresenter.handleSaveUserButtonClick(binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(UserListView.class)));

        return new HorizontalLayout(saveButton, cancelButton);
    }

}
