package com.clean.elearning.user.infrastructure.view;

import com.clean.elearning.shared.view.MainLayout;
import com.clean.elearning.user.adapter.ui.UserListPresenter;
import com.clean.elearning.user.adapter.ui.UserViewModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "/users", layout = MainLayout.class)
@PageTitle("Users")
public class UserListView extends VerticalLayout {

    private final UserListPresenter userListPresenter;
    private final Grid<UserViewModel> grid = new Grid<>(UserViewModel.class);

    @Getter
    private final EditUserFormComponent editUserFormComponent = new EditUserFormComponent();

    public UserListView(UserListPresenter userListPresenter) {
        this.userListPresenter = userListPresenter;
        userListPresenter.setUserListView(this);
        setSizeFull();
        configureGrid();
        configureEditUserFormComponent();
        final var content = createContent();
        add(content);
    }

    @PostConstruct
    private void init() {
        userListPresenter.handlePageLoad();
    }

    public void showUsers(@NonNull List<UserViewModel> users) {
        grid.setItems(users);
    }

    private Component createContent() {
        final var content = new HorizontalLayout(grid, editUserFormComponent);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, editUserFormComponent);
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(UserViewModel::firstName).setHeader("First Name");
        grid.addColumn(UserViewModel::lastName).setHeader("Last Name");
        grid.addColumn(UserViewModel::email).setHeader("Email");
        grid.addColumn(UserViewModel::role).setHeader("Role");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> userListPresenter.handleUserSelection(event.getValue()));
    }

    private void configureEditUserFormComponent() {
        editUserFormComponent.setWidth(25, Unit.PERCENTAGE);
        editUserFormComponent.addSaveListener(event -> userListPresenter.handleSaveUser(event.getUser()));
    }

}
