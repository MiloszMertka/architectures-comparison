package com.classic.elearning.course.infrastructure.view;

import com.classic.elearning.course.domain.QuizResult;
import com.classic.elearning.course.service.QuizService;
import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.domain.User;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

import java.util.Map;

@Route(value = "courses/:courseName/quizzes/:quizName/results", layout = MainLayout.class)
@PageTitle("Quiz results")
@RolesAllowed("TEACHER")
public class QuizResultListView extends VerticalLayout implements BeforeEnterObserver {

    private final QuizService quizService;
    private final Grid<Map.Entry<User, QuizResult>> grid = new Grid<>();
    private String courseName;
    private String quizName;

    public QuizResultListView(QuizService quizService) {
        this.quizService = quizService;
        setSizeFull();
        configureGrid();
        add(grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
        quizName = beforeEnterEvent.getRouteParameters().get("quizName").orElseThrow();
        handlePageLoad(courseName, quizName);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(quizResult -> quizResult.getKey().getFullName()).setHeader("Student");
        grid.addColumn(quizResult -> String.format("%.2f", quizResult.getValue().getEarnedScore())).setHeader("Earned score");
        grid.getColumns().forEach(column -> {
            column.setAutoWidth(true);
            column.setSortable(true);
        });
    }

    public void handlePageLoad(@NonNull String courseName, @NonNull String quizName) {
        final var quizResults = quizService.browseQuizResults(courseName, quizName);
        grid.setItems(quizResults.entrySet());
    }

}
