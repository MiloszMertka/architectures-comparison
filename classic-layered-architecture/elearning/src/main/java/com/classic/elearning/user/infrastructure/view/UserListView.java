package com.classic.elearning.user.infrastructure.view;

import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.infrastructure.ui.UserListUI;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;
import com.classic.elearning.user.infrastructure.ui.presenter.UserListPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users")
@RolesAllowed("ADMIN")
public class UserListView extends VerticalLayout implements UserListUI {

    private final UserListPresenter userListPresenter;
    private final Grid<UserViewModel> grid = new Grid<>(UserViewModel.class);
    private final TextField searchField = new TextField();

    public UserListView(UserListPresenter userListPresenter) {
        this.userListPresenter = userListPresenter;
        userListPresenter.setUserListUI(this);
        setSizeFull();
        final var toolbar = createToolbar();
        configureGrid();
        add(toolbar, grid);
    }

    @PostConstruct
    private void init() {
        userListPresenter.handlePageLoad();
    }

    @Override
    public void showUsers(@NonNull List<UserViewModel> users) {
        grid.setItems(users);
    }

    @Override
    public void navigateToCreateUserFormView() {
        getUI().ifPresent(ui -> ui.navigate(CreateUserFormView.class));
    }

    @Override
    public void navigateToEditUserFormView(@NonNull UpdateUserRequest updateUserRequest) {
        getUI()
                .flatMap(ui -> ui.navigate(EditUserFormView.class, updateUserRequest.getEmail()))
                .ifPresent(view -> view.setUser(updateUserRequest));
    }

    @Override
    public void showDeleteUserConfirmDialog(@NonNull UserViewModel user) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Delete user");
        confirmDialog.setText("Are you sure you want to delete this user?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> userListPresenter.handleDeleteUserConfirm(user));
        confirmDialog.open();
    }

    private Component createToolbar() {
        configureSearchField();
        final var createUserButton = new Button("Create user", new Icon(VaadinIcon.PLUS));
        createUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createUserButton.addClickListener(event -> userListPresenter.handleCreateUserButtonClick());
        return new HorizontalLayout(searchField, createUserButton);
    }

    private void configureSearchField() {
        searchField.setPlaceholder("Search users");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> userListPresenter.handleSearchFieldChange(event.getValue()));
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(UserViewModel::firstName).setHeader("First name");
        grid.addColumn(UserViewModel::lastName).setHeader("Last name");
        grid.addColumn(UserViewModel::email).setHeader("Email");
        grid.addColumn(UserViewModel::role).setHeader("Role");
        grid.getColumns().forEach(column -> column.setSortable(true));
        createEditButtonColumn();
        createDeleteButtonColumn();
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private void createEditButtonColumn() {
        grid.addComponentColumn(user -> {
            final var editIcon = new Icon(VaadinIcon.EDIT);
            final var editButton = new Button(editIcon);
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            editButton.addClickListener(event -> userListPresenter.handleEditUserButtonClick(user));
            return editButton;
        });
    }

    private void createDeleteButtonColumn() {
        grid.addComponentColumn(user -> {
            final var deleteIcon = new Icon(VaadinIcon.TRASH);
            final var deleteButton = new Button(deleteIcon);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(event -> userListPresenter.handleDeleteUserButtonClick(user));
            return deleteButton;
        });
    }

}
