package com.clean.elearning.course.infrastructure.view;

import com.clean.elearning.course.adapter.ui.QuizResultListUI;
import com.clean.elearning.course.adapter.ui.model.QuizResultViewModel;
import com.clean.elearning.course.adapter.ui.presenter.QuizResultListPresenter;
import com.clean.elearning.shared.view.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.lang.NonNull;

import java.util.List;

@Route(value = "courses/:courseName/quizzes/:quizName/results", layout = MainLayout.class)
@PageTitle("Quiz results")
@RolesAllowed("TEACHER")
public class QuizResultListView extends VerticalLayout implements BeforeEnterObserver, QuizResultListUI {

    private final QuizResultListPresenter quizResultListPresenter;
    private final Grid<QuizResultViewModel> grid = new Grid<>(QuizResultViewModel.class);
    private String courseName;
    private String quizName;

    public QuizResultListView(QuizResultListPresenter quizResultListPresenter) {
        this.quizResultListPresenter = quizResultListPresenter;
        this.quizResultListPresenter.setQuizResultListUI(this);
        setSizeFull();
        configureGrid();
        add(grid);
    }

    @Override
    public void showQuizResults(@NonNull List<QuizResultViewModel> quizResults) {
        grid.setItems(quizResults);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        courseName = beforeEnterEvent.getRouteParameters().get("courseName").orElseThrow();
        quizName = beforeEnterEvent.getRouteParameters().get("quizName").orElseThrow();
        quizResultListPresenter.handlePageLoad(courseName, quizName);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(quizResultViewModel -> quizResultViewModel.student().fullName()).setHeader("Student");
        grid.addColumn(QuizResultViewModel::earnedScore).setHeader("Earned score");
        grid.getColumns().forEach(column -> {
            column.setAutoWidth(true);
            column.setSortable(true);
        });
    }

}
