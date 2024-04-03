package com.classic.elearning.course.usecase;

import com.classic.elearning.course.usecase.model.RemoveCourseMaterialModel;

public interface RemoveCourseMaterialUseCase {

    void removeCourseMaterial(String courseName, RemoveCourseMaterialModel removeCourseMaterialModel);

}
