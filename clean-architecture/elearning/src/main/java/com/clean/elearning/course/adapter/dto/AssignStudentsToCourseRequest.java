package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.usecase.model.AssignStudentsToCourseModel;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class AssignStudentsToCourseRequest implements AssignStudentsToCourseModel {

    private Collection<UserViewModel> students;

    @Override
    public Collection<String> getStudentEmails() {
        return students.stream()
                .map(UserViewModel::email)
                .toList();
    }

}
