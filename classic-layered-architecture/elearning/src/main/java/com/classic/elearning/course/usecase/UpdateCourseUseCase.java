package com.classic.elearning.course.usecase;

import com.classic.elearning.course.usecase.model.UpdateCourseModel;

public interface UpdateCourseUseCase {

    void updateCourse(String name, UpdateCourseModel updateCourseModel);

}
