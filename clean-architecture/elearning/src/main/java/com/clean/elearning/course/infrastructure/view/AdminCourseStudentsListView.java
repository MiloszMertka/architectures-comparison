package com.clean.elearning.course.infrastructure.view;

import com.clean.elearning.course.adapter.dto.AssignStudentsToCourseRequest;
import com.clean.elearning.course.adapter.dto.RemoveStudentsFromCourseRequest;
import com.clean.elearning.course.adapter.ui.AdminCourseStudentsListUI;
import com.clean.elearning.course.adapter.ui.presenter.AdminCourseStudentsListPresenter;
import com.clean.elearning.shared.view.MainLayout;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "course/:courseName/students", layout = MainLayout.class)
@PageTitle("Course students")
@PermitAll
public class AdminCourseStudentsListView extends VerticalLayout implements BeforeEnterObserver, AdminCourseStudentsListUI {

    private final AdminCourseStudentsListPresenter adminCourseStudentsListPresenter;
    private final Grid<UserViewModel> grid = new Grid<>(UserViewModel.class);
    private final MultiSelectComboBox<UserViewModel> studentsToAssign = new MultiSelectComboBox<>();
    private String courseName;

    public AdminCourseStudentsListView(AdminCourseStudentsListPresenter adminCourseStudentsListPresenter) {
        this.adminCourseStudentsListPresenter = adminCourseStudentsListPresenter;
        adminCourseStudentsListPresenter.setAdminCourseStudentsListUI(this);
        setSizeFull();
        final var toolbar = createToolbar();
        configureGrid();
        add(toolbar, grid);
    }

    @Override
    public void showCourseStudents(@NonNull List<UserViewModel> students) {
        grid.setItems(students);
    }

    @Override
    public void setStudentsAvailableToAssign(@NonNull List<UserViewModel> students) {
        studentsToAssign.setItems(students);
    }

    @Override
    public void showRemoveStudentsFromCourseConfirmDialog(@NonNull String courseName, @NonNull RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Remove students from course");
        confirmDialog.setText("Are you sure you want to remove selected students from course?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> adminCourseStudentsListPresenter.handleRemoveStudentsFromCourseConfirm(courseName, removeStudentsFromCourseRequest));
        confirmDialog.open();
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
        adminCourseStudentsListPresenter.handlePageLoad(courseName);
    }

    private Component createToolbar() {
        configureStudentsComboBox();
        final var assignStudentsButton = createAssignStudentsButton();
        final var removeStudentsButton = createRemoveStudentsButton();
        return new HorizontalLayout(studentsToAssign, assignStudentsButton, removeStudentsButton);
    }

    private void configureStudentsComboBox() {
        studentsToAssign.setItemLabelGenerator(UserViewModel::fullName);
        studentsToAssign.setPlaceholder("Select students");
    }

    private Component createAssignStudentsButton() {
        final var assignStudentsButton = new Button("Assign students", new Icon(VaadinIcon.PLUS));
        assignStudentsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        assignStudentsButton.addClickListener(event -> {
            final var selectedStudents = studentsToAssign.getValue();
            final var assignStudentsToCourseRequest = new AssignStudentsToCourseRequest();
            assignStudentsToCourseRequest.setStudents(selectedStudents);
            adminCourseStudentsListPresenter.handleAssignStudentsButtonClick(courseName, assignStudentsToCourseRequest);
        });
        return assignStudentsButton;
    }

    private Component createRemoveStudentsButton() {
        final var removeStudentsButton = new Button("Remove students", new Icon(VaadinIcon.TRASH));
        removeStudentsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        removeStudentsButton.addClickListener(event -> {
            final var selectedStudents = grid.getSelectedItems();
            final var removeStudentsFromCourseRequest = new RemoveStudentsFromCourseRequest();
            removeStudentsFromCourseRequest.setStudents(selectedStudents);
            adminCourseStudentsListPresenter.handleRemoveStudentsButtonClick(courseName, removeStudentsFromCourseRequest);
        });
        return removeStudentsButton;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(UserViewModel::firstName).setHeader("First name");
        grid.addColumn(UserViewModel::lastName).setHeader("Last name");
        grid.addColumn(UserViewModel::email).setHeader("Email");
        grid.getColumns().forEach(column -> {
            column.setAutoWidth(true);
            column.setSortable(true);
        });
    }

}
