package com.clean.elearning.user.infrastructure;

import com.clean.elearning.shared.view.MainLayout;
import com.clean.elearning.user.adapter.UserListPresenter;
import com.clean.elearning.user.adapter.UserViewModel;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "/users", layout = MainLayout.class)
@PageTitle("Users")
public class UserListView extends VerticalLayout {

    private final UserListPresenter userListPresenter;
    private final Grid<UserViewModel> grid = new Grid<>(UserViewModel.class);

    public UserListView(UserListPresenter userListPresenter) {
        this.userListPresenter = userListPresenter;
        userListPresenter.setUserListView(this);
        setSizeFull();
        configureGrid();
        add(grid);
    }

    @PostConstruct
    private void init() {
        userListPresenter.handlePageLoad();
    }

    public void showUsers(@NonNull List<UserViewModel> users) {
        grid.setItems(users);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(UserViewModel::firstName).setHeader("First Name");
        grid.addColumn(UserViewModel::lastName).setHeader("Last Name");
        grid.addColumn(UserViewModel::email).setHeader("Email");
        grid.addColumn(UserViewModel::role).setHeader("Role");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
