package com.classic.elearning.user.infrastructure.view;

import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.service.UserService;
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

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users")
@RolesAllowed("ADMIN")
public class UserListView extends VerticalLayout {

    private final UserService userService;
    private final Grid<User> grid = new Grid<>(User.class);
    private final TextField searchField = new TextField();

    public UserListView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        final var toolbar = createToolbar();
        configureGrid();
        add(toolbar, grid);
    }

    @PostConstruct
    private void init() {
        handlePageLoad();
    }

    private Component createToolbar() {
        configureSearchField();
        final var createUserButton = new Button("Create user", new Icon(VaadinIcon.PLUS));
        createUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createUserButton.addClickListener(event -> handleCreateUserButtonClick());
        return new HorizontalLayout(searchField, createUserButton);
    }

    private void configureSearchField() {
        searchField.setPlaceholder("Search users");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> handleSearchFieldChange(event.getValue()));
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.removeAllColumns();
        grid.addColumn(User::getFirstName).setHeader("First name");
        grid.addColumn(User::getLastName).setHeader("Last name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(user -> user.getRole().name().toLowerCase()).setHeader("Role");
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
            editButton.addClickListener(event -> handleEditUserButtonClick(user));
            return editButton;
        });
    }

    private void createDeleteButtonColumn() {
        grid.addComponentColumn(user -> {
            final var deleteIcon = new Icon(VaadinIcon.TRASH);
            final var deleteButton = new Button(deleteIcon);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(event -> handleDeleteUserButtonClick(user));
            return deleteButton;
        });
    }

    private void handlePageLoad() {
        final var users = userService.browseUsers();
        grid.setItems(users);
    }

    private void handleSearchFieldChange(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return;
        }

        final var users = userService.browseUsers(searchText);
        grid.setItems(users);
    }

    private void handleCreateUserButtonClick() {
        navigateToCreateUserFormView();
    }

    private void handleEditUserButtonClick(@NonNull User user) {
        final var updateUserRequest = createUpdateUserRequest(user);
        navigateToEditUserFormView(updateUserRequest);
    }

    private void handleDeleteUserButtonClick(@NonNull User user) {
        showDeleteUserConfirmDialog(user);
    }

    private void handleDeleteUserConfirm(@NonNull User user) {
        userService.deleteUser(user.getEmail());
        handlePageLoad();
    }

    private UpdateUserRequest createUpdateUserRequest(User user) {
        final var updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName(user.getFirstName());
        updateUserRequest.setLastName(user.getLastName());
        updateUserRequest.setEmail(user.getEmail());
        return updateUserRequest;
    }

    private void navigateToCreateUserFormView() {
        getUI().ifPresent(ui -> ui.navigate(CreateUserFormView.class));
    }

    private void navigateToEditUserFormView(@NonNull UpdateUserRequest updateUserRequest) {
        getUI()
                .flatMap(ui -> ui.navigate(EditUserFormView.class, updateUserRequest.getEmail()))
                .ifPresent(view -> view.setUser(updateUserRequest));
    }

    private void showDeleteUserConfirmDialog(@NonNull User user) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Delete user");
        confirmDialog.setText("Are you sure you want to delete this user?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> handleDeleteUserConfirm(user));
        confirmDialog.open();
    }

}
