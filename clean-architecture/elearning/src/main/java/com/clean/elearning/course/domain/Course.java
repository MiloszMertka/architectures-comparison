package com.clean.elearning.course.domain;

import com.clean.elearning.user.domain.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Course {

    private final Long id;
    private final Set<User> students = new HashSet<>();
    private String name;
    private User teacher;

    public Course(String name, User teacher) {
        this(null, name, teacher);
    }

    public Course(Long id, String name, User teacher) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        validateUserIsTeacher(teacher);
        this.teacher = teacher;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void assignStudent(User student) {
        validateUserIsStudent(student);
        students.add(student);
    }

    public void removeStudent(User student) {
        validateUserIsStudent(student);
        students.remove(student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Course course)) {
            return false;
        }

        return Objects.equals(name, course.name);
    }

    private void validateUserIsTeacher(User user) {
        if (!user.isTeacher()) {
            throw new IllegalArgumentException("User is not a teacher");
        }
    }

    private void validateUserIsStudent(User user) {
        if (!user.isStudent()) {
            throw new IllegalArgumentException("User is not a student");
        }
    }

}
