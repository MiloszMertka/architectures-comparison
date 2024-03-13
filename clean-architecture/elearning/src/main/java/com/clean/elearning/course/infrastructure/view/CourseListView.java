package com.clean.elearning.course.infrastructure.view;

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
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.lang.NonNull;

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
        courses.forEach(course -> {
            final var courseName = course.name();
            final var teacher = new Span("Teacher: " + course.teacherName());
            final var courseMaterialsList = createCourseMaterialsList(course.courseMaterials());
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

    private Component createCourseMaterialsList(List<CourseMaterialViewModel> courseMaterialViewModels) {
        final var header = new Span("Course materials");
        final var courseMaterialsListItems = courseMaterialViewModels.stream()
                .map(courseMaterialViewModel -> new ListItem(courseMaterialViewModel.name()))
                .toArray(ListItem[]::new);
        final var courseMaterialsList = new UnorderedList(courseMaterialsListItems);
        final var container = new VerticalLayout(header, courseMaterialsList);
        container.setPadding(false);
        return container;
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
