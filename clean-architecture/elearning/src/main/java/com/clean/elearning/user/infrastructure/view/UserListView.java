package com.clean.elearning.user.infrastructure.view;

import com.clean.elearning.shared.view.MainLayout;
import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.adapter.ui.UserListUI;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import com.clean.elearning.user.adapter.ui.presenter.UserListPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users")
public class UserListView extends VerticalLayout implements UserListUI {

    private final UserListPresenter userListPresenter;
    private final Grid<UserViewModel> grid = new Grid<>(UserViewModel.class);

    public UserListView(UserListPresenter userListPresenter) {
        this.userListPresenter = userListPresenter;
        userListPresenter.setUserListUI(this);
        setSizeFull();
        configureGrid();
        add(grid);
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
    public void navigateToEditUserFormView(@NonNull UpdateUserRequest updateUserRequest) {
        getUI()
                .flatMap(ui -> ui.navigate(EditUserFormView.class, updateUserRequest.getEmail()))
                .ifPresent(view -> view.setUser(updateUserRequest));
    }

    @Override
    public void showDeleteUserConfirmDialog(@NonNull UserViewModel user) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Delete User");
        confirmDialog.setText("Are you sure you want to delete this user?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> userListPresenter.handleDeleteUserConfirm(user));
        confirmDialog.open();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(UserViewModel::firstName).setHeader("First Name");
        grid.addColumn(UserViewModel::lastName).setHeader("Last Name");
        grid.addColumn(UserViewModel::email).setHeader("Email");
        grid.addColumn(UserViewModel::role).setHeader("Role");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        createEditButtonColumn();
        createDeleteButtonColumn();
    }

    private void createEditButtonColumn() {
        grid.addComponentColumn(user -> {
            final var editIcon = new Icon(VaadinIcon.EDIT);
            final var editButton = new Button(editIcon);
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            editButton.addClickListener(click -> userListPresenter.handleEditUserButtonClick(user));
            return editButton;
        }).setWidth("30px");
    }

    private void createDeleteButtonColumn() {
        grid.addComponentColumn(user -> {
            final var deleteIcon = new Icon(VaadinIcon.TRASH);
            final var deleteButton = new Button(deleteIcon);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(click -> userListPresenter.handleDeleteUserButtonClick(user));
            return deleteButton;
        }).setWidth("30px");
    }

}
