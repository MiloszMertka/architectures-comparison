package com.classic.elearning.course.adapter.database;

import com.classic.elearning.course.adapter.database.CourseMaterialSchema;
import com.classic.elearning.course.adapter.database.QuizSchema;
import com.classic.elearning.course.domain.Course;
import com.classic.elearning.user.adapter.database.UserSchema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CourseSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(optional = false)
    private UserSchema teacher;

    @ManyToMany
    private Set<UserSchema> students;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CourseMaterialSchema> courseMaterials;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<com.classic.elearning.course.adapter.database.QuizSchema> quizzes;

    public static CourseSchema fromCourse(@NonNull Course course) {
        final var teacher = UserSchema.fromUser(course.getTeacher());
        final var students = course.getStudents().stream()
                .map(UserSchema::fromUser)
                .collect(Collectors.toSet());
        final var courseMaterials = course.getCourseMaterials().stream()
                .map(CourseMaterialSchema::fromCourseMaterial)
                .collect(Collectors.toSet());
        final var quizzes = course.getQuizzes().stream()
                .map(com.classic.elearning.course.adapter.database.QuizSchema::fromQuiz)
                .toList();
        return new CourseSchema(course.getId(), course.getName(), teacher, students, courseMaterials, quizzes);
    }

    public Course toCourse() {
        final var domainTeacher = teacher.toUser();
        final var domainStudents = students.stream()
                .map(UserSchema::toUser)
                .collect(Collectors.toSet());
        final var domainCourseMaterials = courseMaterials.stream()
                .map(CourseMaterialSchema::toCourseMaterial)
                .collect(Collectors.toSet());
        final var domainQuizzes = quizzes.stream()
                .map(QuizSchema::toQuiz)
                .toList();
        final var course = new Course(id, name, domainTeacher);
        domainStudents.forEach(course::assignStudent);
        domainCourseMaterials.forEach(course::addCourseMaterial);
        domainQuizzes.forEach(course::addQuiz);
        return course;
    }

}
