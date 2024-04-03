package com.classic.elearning.course.adapter.ui.model;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;

import java.util.List;

public record CourseViewModel(
        String name,
        UserViewModel teacher,
        List<UserViewModel> students,
        List<CourseMaterialViewModel> courseMaterials,
        List<QuizViewModel> quizzes
) {

    public static CourseViewModel fromCourse(Course course) {
        final var teacher = UserViewModel.fromUser(course.getTeacher());
        final var students = course.getStudents().stream()
                .map(UserViewModel::fromUser)
                .toList();
        final var courseMaterials = course.getCourseMaterials().stream()
                .map(CourseMaterialViewModel::fromCourseMaterial)
                .toList();
        final var quizzes = course.getQuizzes().stream()
                .map(QuizViewModel::fromQuiz)
                .toList();
        return new CourseViewModel(
                course.getName(),
                teacher,
                students,
                courseMaterials,
                quizzes
        );
    }

    public String teacherName() {
        return teacher().firstName() + " " + teacher().lastName();
    }

}
