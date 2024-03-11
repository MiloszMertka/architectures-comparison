package com.clean.elearning.course.adapter.ui.presenter;

import com.clean.elearning.course.adapter.dto.UpdateCourseRequest;
import com.clean.elearning.course.adapter.ui.AdminCourseListUI;
import com.clean.elearning.course.adapter.ui.model.CourseViewModel;
import com.clean.elearning.course.usecase.BrowseCoursesUseCase;
import com.clean.elearning.course.usecase.DeleteCourseUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AdminCourseListPresenter {

    private final BrowseCoursesUseCase browseCoursesUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;

    @Setter
    private AdminCourseListUI adminCourseListUI;

    @Transactional(readOnly = true)
    public void handlePageLoad() {
        final var courses = browseCoursesUseCase.browseCourses();
        final var courseViewModels = courses.stream()
                .map(CourseViewModel::fromCourse)
                .toList();
        adminCourseListUI.showCourses(courseViewModels);
    }

    @Transactional(readOnly = true)
    public void handleSearchFieldChange(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return;
        }

        final var courses = browseCoursesUseCase.browseCourses(searchText);
        final var courseViewModels = courses.stream()
                .map(CourseViewModel::fromCourse)
                .toList();
        adminCourseListUI.showCourses(courseViewModels);
    }

    public void handleCreateCourseButtonClick() {
        adminCourseListUI.navigateToCreateCourseFormView();
    }

    public void handleEditCourseButtonClick(@NonNull CourseViewModel course) {
        final var updateCourseRequest = createUpdateCourseRequest(course);
        adminCourseListUI.navigateToEditCourseFormView(updateCourseRequest);
    }

    public void handleDeleteCourseButtonClick(@NonNull CourseViewModel course) {
        adminCourseListUI.showDeleteCourseConfirmDialog(course);
    }

    @Transactional
    public void handleDeleteCourseConfirm(@NonNull CourseViewModel course) {
        deleteCourseUseCase.deleteCourse(course.name());
        handlePageLoad();
    }

    public void handleCourseStudentsButtonClick(@NonNull CourseViewModel course) {
        adminCourseListUI.navigateToCourseStudentsListView(course.name());
    }

    private UpdateCourseRequest createUpdateCourseRequest(CourseViewModel course) {
        final var updateCourseRequest = new UpdateCourseRequest();
        updateCourseRequest.setName(course.name());
        updateCourseRequest.setTeacher(course.teacher());
        return updateCourseRequest;
    }

}
