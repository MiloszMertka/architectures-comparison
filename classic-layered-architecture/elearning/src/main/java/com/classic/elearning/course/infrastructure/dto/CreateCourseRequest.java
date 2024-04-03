package com.classic.elearning.course.infrastructure.dto;

import com.classic.elearning.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private User teacher;

    public String getTeacherEmail() {
        return teacher.getEmail();
    }

}
