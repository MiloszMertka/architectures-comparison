package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.adapter.dto.UpdateCourseRequest;
import com.classic.elearning.course.adapter.ui.AdminCourseListUI;
import com.classic.elearning.course.adapter.ui.model.CourseViewModel;
import com.classic.elearning.course.adapter.ui.presenter.AdminCourseListPresenter;
import com.classic.elearning.course.infrastructure.view.AdminCourseStudentsListView;
import com.classic.elearning.course.infrastructure.view.CreateCourseFormView;
import com.classic.elearning.course.infrastructure.view.EditCourseFormView;
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

import java.util.List;

@Route(value = "courses", layout = MainLayout.class)
@PageTitle("Courses")
@RolesAllowed("ADMIN")
public class AdminCourseListView extends VerticalLayout implements AdminCourseListUI {

    private final AdminCourseListPresenter adminCourseListPresenter;
    private final Grid<CourseViewModel> grid = new Grid<>(CourseViewModel.class);
    private final TextField searchField = new TextField();

    public AdminCourseListView(AdminCourseListPresenter adminCourseListPresenter) {
        this.adminCourseListPresenter = adminCourseListPresenter;
        adminCourseListPresenter.setAdminCourseListUI(this);
        setSizeFull();
        final var toolbar = createToolbar();
        configureGrid();
        add(toolbar, grid);
    }

    @PostConstruct
    private void init() {
        adminCourseListPresenter.handlePageLoad();
    }

    @Override
    public void showCourses(@NonNull List<CourseViewModel> courses) {
        grid.setItems(courses);
    }

    @Override
    public void navigateToCreateCourseFormView() {
        getUI().ifPresent(ui -> ui.navigate(CreateCourseFormView.class));
    }

    @Override
    public void navigateToEditCourseFormView(@NonNull UpdateCourseRequest updateCourseRequest) {
        getUI()
                .flatMap(ui -> ui.navigate(EditCourseFormView.class, updateCourseRequest.getName()))
                .ifPresent(view -> view.setCourse(updateCourseRequest));
    }

    @Override
    public void showDeleteCourseConfirmDialog(@NonNull CourseViewModel course) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Delete course");
        confirmDialog.setText("Are you sure you want to delete this course?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> adminCourseListPresenter.handleDeleteCourseConfirm(course));
        confirmDialog.open();
    }

    @Override
    public void navigateToCourseStudentsListView(@NonNull String courseName) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        getUI().ifPresent(ui -> ui.navigate(AdminCourseStudentsListView.class, courseNameRouteParam));
    }

    private Component createToolbar() {
        configureSearchField();
        final var createCourseButton = new Button("Create course", new Icon(VaadinIcon.PLUS));
        createCourseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createCourseButton.addClickListener(event -> adminCourseListPresenter.handleCreateCourseButtonClick());
        return new HorizontalLayout(searchField, createCourseButton);
    }

    private void configureSearchField() {
        searchField.setPlaceholder("Search courses");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> adminCourseListPresenter.handleSearchFieldChange(event.getValue()));
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(CourseViewModel::name).setHeader("Name");
        grid.addColumn(CourseViewModel::teacherName).setHeader("Teacher");
        grid.getColumns().forEach(column -> column.setSortable(true));
        createCourseStudentsButtonColumn();
        createEditButtonColumn();
        createDeleteButtonColumn();
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private void createCourseStudentsButtonColumn() {
        grid.addComponentColumn(course -> {
            final var courseStudentsButton = new Button("Students");
            courseStudentsButton.addClickListener(event -> adminCourseListPresenter.handleCourseStudentsButtonClick(course));
            return courseStudentsButton;
        });
    }

    private void createEditButtonColumn() {
        grid.addComponentColumn(course -> {
            final var editIcon = new Icon(VaadinIcon.EDIT);
            final var editButton = new Button(editIcon);
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            editButton.addClickListener(event -> adminCourseListPresenter.handleEditCourseButtonClick(course));
            return editButton;
        });
    }

    private void createDeleteButtonColumn() {
        grid.addComponentColumn(course -> {
            final var deleteIcon = new Icon(VaadinIcon.TRASH);
            final var deleteButton = new Button(deleteIcon);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(event -> adminCourseListPresenter.handleDeleteCourseButtonClick(course));
            return deleteButton;
        });
    }

}
