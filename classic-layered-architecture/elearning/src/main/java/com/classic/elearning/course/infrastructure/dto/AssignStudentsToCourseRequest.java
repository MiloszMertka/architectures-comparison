package com.classic.elearning.course.infrastructure.dto;

import com.classic.elearning.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class AssignStudentsToCourseRequest {

    private Collection<User> students;

    public Collection<String> getStudentEmails() {
        return students.stream()
                .map(User::getEmail)
                .toList();
    }

}
