package com.classic.elearning.course.adapter.dto;

import com.classic.elearning.course.usecase.model.CreateCourseModel;
import com.classic.elearning.user.adapter.ui.model.UserViewModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseRequest implements CreateCourseModel {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private UserViewModel teacher;

    @Override
    public String getTeacherEmail() {
        return teacher.email();
    }

}
