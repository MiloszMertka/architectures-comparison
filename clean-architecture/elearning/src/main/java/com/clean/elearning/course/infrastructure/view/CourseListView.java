package com.clean.elearning.course.infrastructure.view;

import com.clean.elearning.course.adapter.dto.RemoveCourseMaterialRequest;
import com.clean.elearning.course.adapter.ui.CourseListUI;
import com.clean.elearning.course.adapter.ui.model.CourseMaterialViewModel;
import com.clean.elearning.course.adapter.ui.model.CourseViewModel;
import com.clean.elearning.course.adapter.ui.presenter.CourseListPresenter;
import com.clean.elearning.shared.service.SecurityService;
import com.clean.elearning.shared.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Route(value = "my-courses", layout = MainLayout.class)
@PageTitle("Courses")
@PermitAll
public class CourseListView extends VerticalLayout implements CourseListUI {

    private final SecurityService securityService;
    private final CourseListPresenter courseListPresenter;
    private final Accordion accordion = new Accordion();

    public CourseListView(SecurityService securityService, CourseListPresenter courseListPresenter) {
        this.securityService = securityService;
        this.courseListPresenter = courseListPresenter;
        courseListPresenter.setCourseListUI(this);
        setSizeFull();
        accordion.setWidthFull();
        add(accordion);
    }

    @PostConstruct
    private void init() {
        courseListPresenter.handlePageLoad();
    }

    @Override
    public void showCourses(@NonNull List<CourseViewModel> courses) {
        accordion.getElement().removeAllChildren();
        courses.forEach(course -> {
            final var courseName = course.name();
            final var teacher = new Span("Teacher: " + course.teacherName());
            final var courseMaterialsList = createCourseMaterialsList(courseName, course.courseMaterials());
            final var courseDetails = new VerticalLayout(teacher, courseMaterialsList);
            addAttachCourseMaterialButtonForTeacher(courseDetails, courseName);
            final var coursePanel = accordion.add(courseName, courseDetails);
            coursePanel.addThemeVariants(DetailsVariant.FILLED);
        });
    }

    @Override
    public void navigateToAttachCourseMaterialFormView(@NonNull String courseName) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        getUI().ifPresent(ui -> ui.navigate(AttachCourseMaterialFormView.class, courseNameRouteParam));
    }

    @Override
    public void showRemoveCourseMaterialConfirmDialog(@NonNull String courseName, @NonNull RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Remove course material");
        confirmDialog.setText("Are you sure you want to remove this course material?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Remove");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> courseListPresenter.handleRemoveCourseMaterialConfirm(courseName, removeCourseMaterialRequest));
        confirmDialog.open();
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    private Component createCourseMaterialsList(String courseName, List<CourseMaterialViewModel> courseMaterialViewModels) {
        final var header = new Span("Course materials:");
        final var courseMaterials = courseMaterialViewModels.stream()
                .map(courseMaterialViewModel -> createDownloadLink(courseName, courseMaterialViewModel))
                .toList();
        final var container = new VerticalLayout(header);
        container.add(courseMaterials);
        container.setPadding(false);
        return container;
    }

    private Component createDownloadLink(String courseName, CourseMaterialViewModel courseMaterialViewModel) {
        final var file = courseMaterialViewModel.file();
        final var streamResource = new StreamResource(file.getName(), () -> createStreamFromFile(file));
        final var downloadLink = new Anchor(streamResource, courseMaterialViewModel.name());
        downloadLink.getElement().setAttribute("download", true);
        return addCreteRemoveCourseMaterialButtonForTeacher(downloadLink, courseName, courseMaterialViewModel.name(), file);
    }

    private InputStream createStreamFromFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private Component addCreteRemoveCourseMaterialButtonForTeacher(Component downloadLink, String courseName, String courseMaterialName, File file) {
        if (!securityService.getCurrentUser().isTeacher()) {
            return downloadLink;
        }

        final var removeCourseMaterialButton = createRemoveCourseMaterialButton(courseName, courseMaterialName, file);
        final var container = new HorizontalLayout(downloadLink, removeCourseMaterialButton);
        container.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return container;
    }

    private Component createRemoveCourseMaterialButton(String courseName, String courseMaterialName, File file) {
        final var removeCourseMaterialButton = new Button(new Icon(VaadinIcon.TRASH));
        removeCourseMaterialButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        final var removeCourseMaterialRequest = new RemoveCourseMaterialRequest();
        removeCourseMaterialRequest.setCourseMaterialName(courseMaterialName);
        removeCourseMaterialRequest.setCourseMaterialFile(file);
        removeCourseMaterialButton.addClickListener(event -> courseListPresenter.handleRemoveCourseMaterialButtonClick(courseName, removeCourseMaterialRequest));
        return removeCourseMaterialButton;
    }

    private void addAttachCourseMaterialButtonForTeacher(HasComponents courseDetails, String courseName) {
        if (!securityService.getCurrentUser().isTeacher()) {
            return;
        }

        final var attachCourseMaterialButton = createAttachCourseMaterialButton(courseName);
        courseDetails.add(attachCourseMaterialButton);
    }

    private Component createAttachCourseMaterialButton(String courseName) {
        final var attachCourseMaterialButton = new Button("Attach course material", new Icon(VaadinIcon.UPLOAD));
        attachCourseMaterialButton.addClickListener(event -> courseListPresenter.handleAttachCourseMaterialButtonClick(courseName));
        return attachCourseMaterialButton;
    }

}
