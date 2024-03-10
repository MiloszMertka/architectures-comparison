package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.usecase.model.UpdateCourseModel;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
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
