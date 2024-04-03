package com.classic.elearning.course.adapter.dto;

import com.classic.elearning.course.usecase.model.UpdateCourseModel;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseRequest implements UpdateCourseModel {

    private String name;
    private UserViewModel teacher;

    @Override
    public String getTeacherEmail() {
        return teacher.email();
    }

}
