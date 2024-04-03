package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.infrastructure.dto.UpdateCourseRequest;
import com.classic.elearning.course.service.CourseService;
import com.classic.elearning.shared.view.MainLayout;
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
import com.vaadin.flow.router.RouteParam;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

@Route(value = "courses", layout = MainLayout.class)
@PageTitle("Courses")
@RolesAllowed("ADMIN")
public class AdminCourseListView extends VerticalLayout {

    private final CourseService courseService;
    private final Grid<Course> grid = new Grid<>(Course.class);
    private final TextField searchField = new TextField();

    public AdminCourseListView(CourseService courseService) {
        this.courseService = courseService;
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
        final var createCourseButton = new Button("Create course", new Icon(VaadinIcon.PLUS));
        createCourseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createCourseButton.addClickListener(event -> handleCreateCourseButtonClick());
        return new HorizontalLayout(searchField, createCourseButton);
    }

    private void configureSearchField() {
        searchField.setPlaceholder("Search courses");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> handleSearchFieldChange(event.getValue()));
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.removeAllColumns();
        grid.addColumn(Course::getName).setHeader("Name");
        grid.addColumn(Course::getTeacherName).setHeader("Teacher");
        grid.getColumns().forEach(column -> column.setSortable(true));
        createCourseStudentsButtonColumn();
        createEditButtonColumn();
        createDeleteButtonColumn();
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private void createCourseStudentsButtonColumn() {
        grid.addComponentColumn(course -> {
            final var courseStudentsButton = new Button("Students");
            courseStudentsButton.addClickListener(event -> handleCourseStudentsButtonClick(course));
            return courseStudentsButton;
        });
    }

    private void createEditButtonColumn() {
        grid.addComponentColumn(course -> {
            final var editIcon = new Icon(VaadinIcon.EDIT);
            final var editButton = new Button(editIcon);
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            editButton.addClickListener(event -> handleEditCourseButtonClick(course));
            return editButton;
        });
    }

    private void createDeleteButtonColumn() {
        grid.addComponentColumn(course -> {
            final var deleteIcon = new Icon(VaadinIcon.TRASH);
            final var deleteButton = new Button(deleteIcon);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(event -> handleDeleteCourseButtonClick(course));
            return deleteButton;
        });
    }

    private void handlePageLoad() {
        final var courses = courseService.browseCourses();
        grid.setItems(courses);
    }

    private void handleSearchFieldChange(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return;
        }

        final var courses = courseService.browseCourses(searchText);
        grid.setItems(courses);
    }

    private void handleCreateCourseButtonClick() {
        navigateToCreateCourseFormView();
    }

    private void handleEditCourseButtonClick(@NonNull Course course) {
        final var updateCourseRequest = createUpdateCourseRequest(course);
        navigateToEditCourseFormView(updateCourseRequest);
    }

    private void handleDeleteCourseButtonClick(@NonNull Course course) {
        showDeleteCourseConfirmDialog(course);
    }

    private void handleDeleteCourseConfirm(@NonNull Course course) {
        courseService.deleteCourse(course.getName());
        handlePageLoad();
    }

    private void handleCourseStudentsButtonClick(@NonNull Course course) {
        navigateToCourseStudentsListView(course.getName());
    }

    private UpdateCourseRequest createUpdateCourseRequest(Course course) {
        final var updateCourseRequest = new UpdateCourseRequest();
        updateCourseRequest.setName(course.getName());
        updateCourseRequest.setTeacher(course.getTeacher());
        return updateCourseRequest;
    }

    private void navigateToCreateCourseFormView() {
        getUI().ifPresent(ui -> ui.navigate(CreateCourseFormView.class));
    }

    private void navigateToEditCourseFormView(@NonNull UpdateCourseRequest updateCourseRequest) {
        getUI()
                .flatMap(ui -> ui.navigate(EditCourseFormView.class, updateCourseRequest.getName()))
                .ifPresent(view -> view.setCourse(updateCourseRequest));
    }

    private void showDeleteCourseConfirmDialog(@NonNull Course course) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Delete course");
        confirmDialog.setText("Are you sure you want to delete this course?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> handleDeleteCourseConfirm(course));
        confirmDialog.open();
    }

    private void navigateToCourseStudentsListView(@NonNull String courseName) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        getUI().ifPresent(ui -> ui.navigate(AdminCourseStudentsListView.class, courseNameRouteParam));
    }

}
