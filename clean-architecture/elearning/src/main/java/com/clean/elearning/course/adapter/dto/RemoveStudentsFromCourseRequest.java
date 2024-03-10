package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.usecase.model.RemoveStudentsFromCourseModel;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class RemoveStudentsFromCourseRequest implements RemoveStudentsFromCourseModel {

    private Collection<UserViewModel> students;

    @Override
    public Collection<String> getStudentEmails() {
        return students.stream()
                .map(UserViewModel::email)
                .toList();
    }

}
