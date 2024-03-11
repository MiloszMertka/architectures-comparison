package com.clean.elearning.course.adapter.database;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.user.adapter.database.UserSchema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

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

    public static CourseSchema fromCourse(@NonNull Course course) {
        final var teacher = UserSchema.fromUser(course.getTeacher());
        final var students = course.getStudents().stream()
                .map(UserSchema::fromUser)
                .collect(Collectors.toSet());
        return new CourseSchema(course.getId(), course.getName(), teacher, students);
    }

    public Course toCourse() {
        final var domainTeacher = teacher.toUser();
        final var domainStudents = students.stream()
                .map(UserSchema::toUser)
                .collect(Collectors.toSet());
        final var course = new Course(id, name, domainTeacher);
        domainStudents.forEach(course::assignStudent);
        return course;
    }

}
