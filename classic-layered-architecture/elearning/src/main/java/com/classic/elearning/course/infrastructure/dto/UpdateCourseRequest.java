package com.classic.elearning.course.infrastructure.dto;

import com.classic.elearning.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseRequest {

    private String name;
    private User teacher;

    public String getTeacherEmail() {
        return teacher.getEmail();
    }

}
