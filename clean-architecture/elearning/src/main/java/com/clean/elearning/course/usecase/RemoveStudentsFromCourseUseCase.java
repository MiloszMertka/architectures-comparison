package com.clean.elearning.course.usecase;

import com.clean.elearning.course.usecase.model.RemoveStudentsFromCourseModel;

public interface RemoveStudentsFromCourseUseCase {

    void removeStudentsFromCourse(String name, RemoveStudentsFromCourseModel removeStudentsFromCourseModel);

}
