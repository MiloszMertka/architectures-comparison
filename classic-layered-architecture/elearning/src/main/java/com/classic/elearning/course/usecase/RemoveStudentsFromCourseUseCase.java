package com.classic.elearning.course.usecase;

import com.classic.elearning.course.usecase.model.RemoveStudentsFromCourseModel;

public interface RemoveStudentsFromCourseUseCase {

    void removeStudentsFromCourse(String name, RemoveStudentsFromCourseModel removeStudentsFromCourseModel);

}
