package com.classic.elearning.course.domain;

import com.classic.elearning.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(optional = false)
    private User teacher;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> students = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CourseMaterial> courseMaterials = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Quiz> quizzes = new ArrayList<>();

    public Course(String name, User teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    public String getTeacherName() {
        return teacher.getFirstName() + " " + teacher.getLastName();
    }

    public void assignStudent(User student) {
        validateUserIsStudent(student);
        students.add(student);
    }

    public void removeStudent(User student) {
        validateUserIsStudent(student);
        students.remove(student);
    }

    public void addCourseMaterial(CourseMaterial courseMaterial) {
        courseMaterials.add(courseMaterial);
    }

    public void removeCourseMaterial(CourseMaterial courseMaterial) {
        courseMaterials.remove(courseMaterial);
    }

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    public void removeQuiz(Quiz quiz) {
        quizzes.remove(quiz);
    }

    public boolean isStudentEnrolled(User student) {
        return students.contains(student);
    }

    private void validateUserIsStudent(User user) {
        if (!user.isStudent()) {
            throw new IllegalArgumentException("User is not a student");
        }
    }

}
