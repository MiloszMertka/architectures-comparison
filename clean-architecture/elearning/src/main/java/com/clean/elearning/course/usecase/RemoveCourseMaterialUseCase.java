package com.clean.elearning.course.usecase;

import com.clean.elearning.course.usecase.model.RemoveCourseMaterialModel;

public interface RemoveCourseMaterialUseCase {

    void removeCourseMaterial(String courseName, RemoveCourseMaterialModel removeCourseMaterialModel);

}
