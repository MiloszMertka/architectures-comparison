package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.domain.*;
import com.classic.elearning.course.infrastructure.dto.AnswerDto;
import com.classic.elearning.course.infrastructure.dto.QuestionDto;
import com.classic.elearning.course.infrastructure.dto.RemoveCourseMaterialRequest;
import com.classic.elearning.course.infrastructure.dto.UpdateQuizRequest;
import com.classic.elearning.course.service.CourseMaterialService;
import com.classic.elearning.course.service.CourseService;
import com.classic.elearning.course.service.QuizService;
import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.domain.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
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
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "my-courses", layout = MainLayout.class)
@PageTitle("Courses")
@PermitAll
public class CourseListView extends VerticalLayout {

    private final AuthenticationContext authenticationContext;
    private final CourseService courseService;
    private final CourseMaterialService courseMaterialService;
    private final QuizService quizService;
    private final Accordion accordion = new Accordion();

    public CourseListView(AuthenticationContext authenticationContext, CourseService courseService, CourseMaterialService courseMaterialService, QuizService quizService) {
        this.authenticationContext = authenticationContext;
        this.courseService = courseService;
        this.courseMaterialService = courseMaterialService;
        this.quizService = quizService;
        setSizeFull();
        accordion.setWidthFull();
        add(accordion);
    }

    @PostConstruct
    private void init() {
        handlePageLoad();
    }

    private Component createCourseMaterialsList(String courseName, Set<CourseMaterial> courseMaterials) {
        final var header = new H3("Course materials");
        final var courseMaterialLinks = courseMaterials.stream()
                .map(courseMaterial -> createDownloadLink(courseName, courseMaterial))
                .toList();
        final var container = new VerticalLayout(header);
        container.add(courseMaterialLinks);
        container.setPadding(false);
        return container;
    }

    private Component createDownloadLink(String courseName, CourseMaterial courseMaterial) {
        final var file = courseMaterial.getFile();
        final var streamResource = new StreamResource(file.getName(), () -> createStreamFromFile(file));
        final var downloadLink = new Anchor(streamResource, courseMaterial.getName());
        downloadLink.getElement().setAttribute("download", true);
        return addRemoveCourseMaterialButtonForTeacher(downloadLink, courseName, courseMaterial.getName(), file);
    }

    private InputStream createStreamFromFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private Component addRemoveCourseMaterialButtonForTeacher(Component downloadLink, String courseName, String courseMaterialName, File file) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isTeacher()) {
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
        removeCourseMaterialButton.addClickListener(event -> handleRemoveCourseMaterialButtonClick(courseName, removeCourseMaterialRequest));
        return removeCourseMaterialButton;
    }

    private void addAttachCourseMaterialButtonForTeacher(HasComponents courseDetails, String courseName) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isTeacher()) {
            return;
        }

        final var attachCourseMaterialButton = createAttachCourseMaterialButton(courseName);
        courseDetails.add(attachCourseMaterialButton);
    }

    private Component createAttachCourseMaterialButton(String courseName) {
        final var attachCourseMaterialButton = new Button("Attach course material", new Icon(VaadinIcon.UPLOAD));
        attachCourseMaterialButton.addClickListener(event -> handleAttachCourseMaterialButtonClick(courseName));
        return attachCourseMaterialButton;
    }

    private Component createQuizzesList(String courseName, List<Quiz> quizzes) {
        final var header = new H3("Quizzes");
        final var quizzesList = quizzes.stream()
                .map(quiz -> {
                    final var name = new H4(quiz.getName());
                    final var openingTime = new Span("Opening date: " + Quiz.formatDateTime(quiz.getOpeningTime()));
                    final var closingTime = new Span("Closing date: " + Quiz.formatDateTime(quiz.getClosingTime()));
                    final var container = new VerticalLayout(name, openingTime, closingTime);
                    container.setPadding(false);
                    addSolveQuizButtonForStudent(container, courseName, quiz);
                    addShowQuizResultsButtonForTeacher(container, courseName, quiz);
                    addEditQuizButtonForTeacher(container, courseName, quiz);
                    addDeleteQuizButtonForTeacher(container, courseName, quiz);
                    return (Component) container;
                })
                .toList();
        final var container = new VerticalLayout(header);
        container.add(quizzesList);
        container.setPadding(false);
        return container;
    }

    private void addSolveQuizButtonForStudent(HasComponents courseDetails, String courseName, Quiz quiz) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isStudent()) {
            return;
        }

        final var currentTime = Instant.now();
        if (currentTime.isBefore(quiz.getOpeningTime()) || currentTime.isAfter(quiz.getClosingTime())) {
            return;
        }

        if (quiz.isSolvedByStudent(user)) {
            final var earnedScoreLabel = createEarnedScoreLabel(quiz, user);
            courseDetails.add(earnedScoreLabel);
            return;
        }

        final var solveQuizButton = createSolveQuizButton(courseName, quiz);
        courseDetails.add(solveQuizButton);
    }

    private Component createEarnedScoreLabel(Quiz quiz, User student) {
        final var quizResult = quiz.getQuizResultByStudent(student);
        return new Span("Earned score: " + String.format("%.2f", quizResult.getEarnedScore()) + " / " + String.format("%.2f", quiz.getTotalScore()));
    }

    private Component createSolveQuizButton(String courseName, Quiz quiz) {
        final var solveQuizButton = new Button("Solve quiz", new Icon(VaadinIcon.PENCIL));
        solveQuizButton.addClickListener(event -> handleSolveQuizButtonClick(courseName, quiz));
        return solveQuizButton;
    }

    private void addDeleteQuizButtonForTeacher(HasComponents courseDetails, String courseName, Quiz quiz) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isTeacher()) {
            return;
        }

        final var deleteQuizButton = createDeleteQuizButton(courseName, quiz);
        courseDetails.add(deleteQuizButton);
    }

    private Component createDeleteQuizButton(String courseName, Quiz quiz) {
        final var deleteQuizButton = new Button("Delete quiz", new Icon(VaadinIcon.TRASH));
        deleteQuizButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteQuizButton.addClickListener(event -> handleDeleteQuizButtonClick(courseName, quiz.getName()));
        return deleteQuizButton;
    }

    private void addShowQuizResultsButtonForTeacher(HasComponents courseDetails, String courseName, Quiz quiz) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isTeacher()) {
            return;
        }

        final var showQuizResultsButton = createShowQuizResultsButton(courseName, quiz);
        courseDetails.add(showQuizResultsButton);
    }

    private Component createShowQuizResultsButton(String courseName, Quiz quiz) {
        final var showQuizResultsButton = new Button("Show quiz results");
        showQuizResultsButton.addClickListener(event -> handleShowQuizResultsButtonClick(courseName, quiz.getName()));
        return showQuizResultsButton;
    }

    private void addEditQuizButtonForTeacher(HasComponents courseDetails, String courseName, Quiz quiz) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isTeacher()) {
            return;
        }

        final var editQuizButton = createEditQuizButton(courseName, quiz);
        courseDetails.add(editQuizButton);
    }

    private Component createEditQuizButton(String courseName, Quiz quiz) {
        final var editQuizButton = new Button("Edit quiz", new Icon(VaadinIcon.EDIT));
        editQuizButton.addClickListener(event -> handleEditQuizButtonClick(courseName, quiz));
        return editQuizButton;
    }

    private void addCreateQuizButtonForTeacher(HasComponents courseDetails, String courseName) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!user.isTeacher()) {
            return;
        }

        final var createQuizButton = createCreateQuizButton(courseName);
        courseDetails.add(createQuizButton);
    }

    private Component createCreateQuizButton(String courseName) {
        final var createQuizButton = new Button("Create quiz", new Icon(VaadinIcon.PLUS));
        createQuizButton.addClickListener(event -> handleCreateQuizButtonClick(courseName));
        return createQuizButton;
    }

    private void handlePageLoad() {
        final var courses = courseService.browseUserCourses();
        showCourses(courses);
    }

    private void handleAttachCourseMaterialButtonClick(@NonNull String courseName) {
        navigateToAttachCourseMaterialFormView(courseName);
    }

    private void handleRemoveCourseMaterialButtonClick(@NonNull String courseName, @NonNull RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        showRemoveCourseMaterialConfirmDialog(courseName, removeCourseMaterialRequest);
    }

    private void handleRemoveCourseMaterialConfirm(@NonNull String courseName, @NonNull RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        try {
            courseMaterialService.removeCourseMaterial(courseName, removeCourseMaterialRequest);
            handlePageLoad();
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void handleCreateQuizButtonClick(@NonNull String courseName) {
        navigateToCreateQuizFormView(courseName);
    }

    private void handleShowQuizResultsButtonClick(@NonNull String courseName, @NonNull String quizName) {
        navigateToQuizResultListView(courseName, quizName);
    }

    private void handleEditQuizButtonClick(@NonNull String courseName, @NonNull Quiz quiz) {
        final var updateQuizRequest = createUpdateQuizRequest(quiz);
        navigateToEditQuizFormView(courseName, updateQuizRequest);
    }

    private void handleDeleteQuizButtonClick(@NonNull String courseName, @NonNull String quizName) {
        showDeleteQuizConfirmDialog(courseName, quizName);
    }

    private void handleDeleteQuizConfirm(@NonNull String courseName, @NonNull String quizName) {
        try {
            quizService.deleteQuiz(courseName, quizName);
            handlePageLoad();
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void handleSolveQuizButtonClick(@NonNull String courseName, @NonNull Quiz quiz) {
        try {
            navigateToSolveQuizFormView(courseName, quiz);
        } catch (Exception exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private UpdateQuizRequest createUpdateQuizRequest(Quiz quiz) {
        final var updateQuizRequest = new UpdateQuizRequest();
        updateQuizRequest.setName(quiz.getName());
        updateQuizRequest.setOpeningTime(LocalDateTime.ofInstant(quiz.getOpeningTime(), ZoneId.systemDefault()));
        updateQuizRequest.setClosingTime(LocalDateTime.ofInstant(quiz.getClosingTime(), ZoneId.systemDefault()));
        updateQuizRequest.setQuestions(quiz.getQuestions().stream()
                .map(this::mapQuestionToQuestionDto)
                .collect(Collectors.toList()));
        return updateQuizRequest;
    }

    private QuestionDto mapQuestionToQuestionDto(Question question) {
        final var questionDto = new QuestionDto();
        questionDto.setContent(question.getContent());
        questionDto.setScore(question.getScore());
        questionDto.setAnswers(question.getAnswers().stream()
                .map(this::mapAnswerToAnswerDto)
                .collect(Collectors.toList()));
        return questionDto;
    }

    private AnswerDto mapAnswerToAnswerDto(Answer answerViewModel) {
        final var answerDto = new AnswerDto();
        answerDto.setContent(answerViewModel.getContent());
        answerDto.setCorrect(answerViewModel.isCorrect());
        return answerDto;
    }

    private void showCourses(@NonNull List<Course> courses) {
        accordion.getElement().removeAllChildren();
        courses.forEach(course -> {
            final var courseName = course.getName();
            final var teacher = new Span("Teacher: " + course.getTeacher().getFullName());
            final var courseMaterialsList = createCourseMaterialsList(courseName, course.getCourseMaterials());
            final var quizzesList = createQuizzesList(courseName, course.getQuizzes());
            final var courseDetails = new VerticalLayout(teacher, courseMaterialsList);
            addAttachCourseMaterialButtonForTeacher(courseDetails, courseName);
            courseDetails.add(quizzesList);
            addCreateQuizButtonForTeacher(courseDetails, courseName);
            final var coursePanel = accordion.add(courseName, courseDetails);
            coursePanel.addThemeVariants(DetailsVariant.FILLED);
        });
    }

    private void navigateToAttachCourseMaterialFormView(@NonNull String courseName) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        getUI().ifPresent(ui -> ui.navigate(AttachCourseMaterialFormView.class, courseNameRouteParam));
    }

    private void showRemoveCourseMaterialConfirmDialog(@NonNull String courseName, @NonNull RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Remove course material");
        confirmDialog.setText("Are you sure you want to remove this course material?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Remove");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> handleRemoveCourseMaterialConfirm(courseName, removeCourseMaterialRequest));
        confirmDialog.open();
    }

    private void navigateToCreateQuizFormView(@NonNull String courseName) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        getUI().ifPresent(ui -> ui.navigate(CreateQuizFormView.class, courseNameRouteParam));
    }

    private void navigateToQuizResultListView(@NonNull String courseName, @NonNull String quizName) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        final var quizNameRouteParam = new RouteParam("quizName", quizName);
        getUI().ifPresent(ui -> ui.navigate(QuizResultListView.class, courseNameRouteParam, quizNameRouteParam));
    }

    private void navigateToEditQuizFormView(@NonNull String courseName, @NonNull UpdateQuizRequest updateQuizRequest) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        final var quizNameRouteParam = new RouteParam("quizName", updateQuizRequest.getName());
        getUI()
                .flatMap(ui -> ui.navigate(EditQuizFormView.class, courseNameRouteParam, quizNameRouteParam))
                .ifPresent(view -> view.setQuiz(updateQuizRequest));
    }

    private void showDeleteQuizConfirmDialog(@NonNull String courseName, @NonNull String quizName) {
        final var confirmDialog = new ConfirmDialog();
        confirmDialog.setCancelable(true);
        confirmDialog.setHeader("Delete quiz");
        confirmDialog.setText("Are you sure you want to delete this quiz?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_PRIMARY.getVariantName() + " " + ButtonVariant.LUMO_ERROR.getVariantName());
        confirmDialog.addConfirmListener(event -> handleDeleteQuizConfirm(courseName, quizName));
        confirmDialog.open();
    }


    private void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    private void navigateToSolveQuizFormView(@NonNull String courseName, @NonNull Quiz quiz) {
        final var courseNameRouteParam = new RouteParam("courseName", courseName);
        final var quizNameRouteParam = new RouteParam("quizName", quiz.getName());
        getUI()
                .flatMap(ui -> ui.navigate(SolveQuizFormView.class, courseNameRouteParam, quizNameRouteParam))
                .ifPresent(view -> view.setQuiz(quiz));
    }

}
