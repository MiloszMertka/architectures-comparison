package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.infrastructure.dto.AssignStudentsToCourseRequest;
import com.classic.elearning.course.infrastructure.dto.RemoveStudentsFromCourseRequest;
import com.classic.elearning.course.service.CourseService;
import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.service.UserService;
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
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

@Route(value = "courses/:courseName/students", layout = MainLayout.class)
@PageTitle("Course students")
@RolesAllowed("ADMIN")
public class AdminCourseStudentsListView extends VerticalLayout implements BeforeEnterObserver {

    private final CourseService courseService;
    private final UserService userService;
    private final Grid<User> grid = new Grid<>(User.class);
    private final MultiSelectComboBox<User> studentsToAssign = new MultiSelectComboBox<>();
    private String courseName;

    public AdminCourseStudentsListView(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
        setSizeFull();
        final var toolbar = createToolbar();
        configureGrid();
        add(toolbar, grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
        handlePageLoad(courseName);
    }

    private Component createToolbar() {
        configureStudentsComboBox();
        final var assignStudentsButton = createAssignStudentsButton();
        final var removeStudentsButton = createRemoveStudentsButton();
        return new HorizontalLayout(studentsToAssign, assignStudentsButton, removeStudentsButton);
    }

    private void configureStudentsComboBox() {
        studentsToAssign.setItemLabelGenerator(User::getFullName);
        studentsToAssign.setPlaceholder("Select students");
    }

    private Component createAssignStudentsButton() {
        final var assignStudentsButton = new Button("Assign students", new Icon(VaadinIcon.PLUS));
        assignStudentsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        assignStudentsButton.addClickListener(event -> {
            final var selectedStudents = studentsToAssign.getValue();
            final var assignStudentsToCourseRequest = new AssignStudentsToCourseRequest();
            assignStudentsToCourseRequest.setStudents(selectedStudents);
            handleAssignStudentsButtonClick(courseName, assignStudentsToCourseRequest);
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
            handleRemoveStudentsButtonClick(courseName, removeStudentsFromCourseRequest);
        });
        return removeStudentsButton;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.removeAllColumns();
        grid.addColumn(User::getFirstName).setHeader("First name");
        grid.addColumn(User::getLastName).setHeader("Last name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.getColumns().forEach(column -> {
            column.setAutoWidth(true);
            column.setSortable(true);
        });
    }

    private void handlePageLoad(@NonNull String courseName) {
        final var course = courseService.viewCourse(courseName);
        final var courseStudents = course.getStudents();
        final var studentsAvailableToAssign = userService.browseUsers().stream()
                .filter(User::isStudent)
                .filter(user -> !course.getStudents().contains(user))
                .toList();
        grid.setItems(courseStudents);
        studentsToAssign.setItems(studentsAvailableToAssign);
    }

    private void handleAssignStudentsButtonClick(@NonNull String courseName, @NonNull AssignStudentsToCourseRequest assignStudentsToCourseRequest) {
        try {
            courseService.assignStudentsToCourse(courseName, assignStudentsToCourseRequest);
            handlePageLoad(courseName);
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void handleRemoveStudentsButtonClick(@NonNull String courseName, @NonNull RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        showRemoveStudentsFromCourseConfirmDialog(courseName, removeStudentsFromCourseRequest);
    }

    private void handleRemoveStudentsFromCourseConfirm(@NonNull String courseName, @NonNull RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        try {
            courseService.removeStudentsFromCourse(courseName, removeStudentsFromCourseRequest);
            handlePageLoad(courseName);
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void showRemoveStudentsFromCourseConfirmDialog(@NonNull String courseName, @NonNull RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Remove students from course");
        confirmDialog.setText("Are you sure you want to remove selected students from course?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> handleRemoveStudentsFromCourseConfirm(courseName, removeStudentsFromCourseRequest));
        confirmDialog.open();
    }

    private void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

}
