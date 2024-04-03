package com.classic.elearning.course.service;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.infrastructure.dto.AssignStudentsToCourseRequest;
import com.classic.elearning.course.infrastructure.dto.CreateCourseRequest;
import com.classic.elearning.course.infrastructure.dto.RemoveStudentsFromCourseRequest;
import com.classic.elearning.course.infrastructure.dto.UpdateCourseRequest;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Course> browseCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Course> browseCourses(String searchText) {
        return courseRepository.search(searchText);
    }

    @Transactional(readOnly = true)
    public List<Course> browseUserCourses() {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        return courseRepository.findAllByUser(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void createCourse(CreateCourseRequest createCourseRequest) {
        validateCourseDoesNotExist(createCourseRequest.getName());
        final var course = createCourseFromModel(createCourseRequest);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCourse(String name, UpdateCourseRequest updateCourseRequest) {
        validateNameIsNotTaken(name, updateCourseRequest.getName());
        final var course = getCourseByName(name);
        updateCourseFromModel(course, updateCourseRequest);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(String name) {
        validateCourseExists(name);
        courseRepository.deleteByName(name);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Course viewCourse(String courseName) {
        return courseRepository.findByName(courseName).orElseThrow();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void assignStudentsToCourse(String name, AssignStudentsToCourseRequest assignStudentsToCourseRequest) {
        final var course = getCourseByName(name);
        assignStudentsToCourseRequest.getStudentEmails().stream()
                .map(this::getUserByEmail)
                .forEach(course::assignStudent);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void removeStudentsFromCourse(String name, RemoveStudentsFromCourseRequest removeStudentsFromCourseRequest) {
        final var course = getCourseByName(name);
        removeStudentsFromCourseRequest.getStudentEmails().stream()
                .map(this::getUserByEmail)
                .forEach(course::removeStudent);
        courseRepository.save(course);
    }

    private void validateCourseExists(String name) {
        if (!courseRepository.existsByName(name)) {
            throw new IllegalStateException("Course with name " + name + " does not exist");
        }
    }

    private void validateNameIsNotTaken(String oldName, String newName) {
        if (!oldName.equals(newName) && courseRepository.existsByName(newName)) {
            throw new IllegalStateException("Course with name " + newName + " already exists");
        }
    }

    private Course getCourseByName(String name) {
        return courseRepository.findByName(name).orElseThrow();
    }

    private void updateCourseFromModel(Course course, UpdateCourseRequest updateCourseRequest) {
        final var teacher = getUserByEmail(updateCourseRequest.getTeacherEmail());
        course.setName(updateCourseRequest.getName());
        course.setTeacher(teacher);
    }

    private void validateCourseDoesNotExist(String name) {
        if (courseRepository.existsByName(name)) {
            throw new IllegalStateException("Course with name " + name + " already exists");
        }
    }

    private Course createCourseFromModel(CreateCourseRequest createCourseRequest) {
        final var teacher = getUserByEmail(createCourseRequest.getTeacherEmail());
        return new Course(createCourseRequest.getName(), teacher);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

}
