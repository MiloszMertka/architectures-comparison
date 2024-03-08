package com.clean.elearning.user.infrastructure.view;

import com.clean.elearning.shared.view.MainLayout;
import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.adapter.ui.EditUserFormPresenter;
import com.clean.elearning.user.domain.Role;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "users/edit", layout = MainLayout.class)
public class EditUserFormView extends FormLayout implements HasUrlParameter<String> {

    private final EditUserFormPresenter editUserFormPresenter;
    private final BeanValidationBinder<UpdateUserRequest> binder = new BeanValidationBinder<>(UpdateUserRequest.class);
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("Email");
    private final ComboBox<Role> role = new ComboBox<>("Role");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    public EditUserFormView(EditUserFormPresenter editUserFormPresenter) {
        this.editUserFormPresenter = editUserFormPresenter;
        editUserFormPresenter.setEditUserFormView(this);
        binder.bindInstanceFields(this);
        role.setItems(Role.values());
        role.setItemLabelGenerator(role -> role.name().toLowerCase());
        add(firstName, lastName, email, role, createButtonsLayout());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String userEmail) {
    }

    public void setUser(UpdateUserRequest user) {
        binder.setBean(user);
    }

    public boolean isFormValid() {
        return binder.isValid();
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> editUserFormPresenter.handleSaveUserButtonClick(binder.getBean()));
        cancelButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate("/users")));

        return new HorizontalLayout(saveButton, cancelButton);
    }

}
