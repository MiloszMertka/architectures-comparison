package com.clean.elearning.user.infrastructure.view;

import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.domain.Role;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public class EditUserFormComponent extends FormLayout {

    private final BeanValidationBinder<UpdateUserRequest> binder = new BeanValidationBinder<>(UpdateUserRequest.class);
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("Email");
    private final ComboBox<Role> role = new ComboBox<>("Role");
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final Button closeButton = new Button("Cancel");

    public EditUserFormComponent() {
        binder.bindInstanceFields(this);
        role.setItems(Role.values());
        role.setItemLabelGenerator(role -> role.name().toLowerCase());
        add(firstName, lastName, email, role, createButtonsLayout());
    }

    public void setUser(UpdateUserRequest user) {
        binder.setBean(user);
    }

    public void openEditor() {
        setVisible(true);
    }

    public void closeEditor() {
        setUser(null);
        setVisible(false);
    }

    public Registration addSaveListener(ComponentEventListener<SaveUserEvent> listener) {
        return addListener(SaveUserEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteUserEvent> listener) {
        return addListener(DeleteUserEvent.class, listener);
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> saveUser());
        deleteButton.addClickListener(click -> deleteUser());
        closeButton.addClickListener(click -> closeEditor());

        return new HorizontalLayout(saveButton, deleteButton, closeButton);
    }

    private void saveUser() {
        if (!binder.isValid()) {
            return;
        }

        fireEvent(new SaveUserEvent(this, binder.getBean()));
    }

    private void deleteUser() {
        fireEvent(new DeleteUserEvent(this, binder.getBean()));
    }

    @Getter
    public static abstract class UserFormEvent extends ComponentEvent<EditUserFormComponent> {

        private final UpdateUserRequest user;

        protected UserFormEvent(EditUserFormComponent source, UpdateUserRequest user) {
            super(source, false);
            this.user = user;
        }

    }

    public static class SaveUserEvent extends UserFormEvent {

        public SaveUserEvent(EditUserFormComponent source, UpdateUserRequest user) {
            super(source, user);
        }

    }

    public static class DeleteUserEvent extends UserFormEvent {

        public DeleteUserEvent(EditUserFormComponent source, UpdateUserRequest user) {
            super(source, user);
        }

    }

}
