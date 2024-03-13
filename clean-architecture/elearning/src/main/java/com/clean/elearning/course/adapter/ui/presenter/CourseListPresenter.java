package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.ui.CourseListUI;
import com.clean.elearning.course.adapter.ui.model.CourseViewModel;
import com.clean.elearning.course.usecase.BrowseUserCoursesUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CourseListPresenter {

    private final BrowseUserCoursesUseCase browseUserCoursesUseCase;

    @Setter
    private CourseListUI courseListUI;

    @Transactional(readOnly = true)
    public void handlePageLoad() {
        final var courses = browseUserCoursesUseCase.browseUserCourses();
        final var courseViewModels = courses.stream()
                .map(CourseViewModel::fromCourse)
                .toList();
        courseListUI.showCourses(courseViewModels);
    }
    
    public void handleAttachCourseMaterialButtonClick(@NonNull String courseName) {
        courseListUI.navigateToAttachCourseMaterialFormView(courseName);
    }

}
