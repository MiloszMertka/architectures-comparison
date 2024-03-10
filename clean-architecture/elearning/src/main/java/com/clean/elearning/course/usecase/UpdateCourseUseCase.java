package com.clean.elearning.course.usecase;

import com.clean.elearning.course.usecase.model.UpdateCourseModel;

public interface UpdateCourseUseCase {

    void updateCourse(String name, UpdateCourseModel updateCourseModel);

}
