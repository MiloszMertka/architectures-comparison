package com.clean.elearning.course.adapter.ui.model;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;

import java.util.List;

public record CourseViewModel(
        String name,
        UserViewModel teacher,
        List<UserViewModel> students
) {

    public static CourseViewModel fromCourse(Course course) {
        final var teacher = UserViewModel.fromUser(course.getTeacher());
        final var students = course.getStudents().stream()
                .map(UserViewModel::fromUser)
                .toList();
        return new CourseViewModel(
                course.getName(),
                teacher,
                students
        );
    }

    public String teacherName() {
        return teacher().firstName() + " " + teacher().lastName();
    }

}
