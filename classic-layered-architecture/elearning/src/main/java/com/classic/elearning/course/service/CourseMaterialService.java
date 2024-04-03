package com.classic.elearning.course.service;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseMaterial;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.infrastructure.dto.AttachCourseMaterialRequest;
import com.classic.elearning.course.infrastructure.dto.RemoveCourseMaterialRequest;
import com.classic.elearning.shared.service.FileService;
import com.classic.elearning.user.domain.User;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CourseMaterialService {

    private static final String COURSE_MATERIALS_DIRECTORY = "course_materials";
    private final CourseRepository courseRepository;
    private final FileService fileService;
    private final AuthenticationContext authenticationContext;

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void attachCourseMaterial(String courseName, AttachCourseMaterialRequest attachCourseMaterialRequest) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        final var savedFile = saveFile(attachCourseMaterialRequest);
        final var courseMaterial = new CourseMaterial(null, attachCourseMaterialRequest.getName(), savedFile.getPath());
        course.addCourseMaterial(courseMaterial);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void removeCourseMaterial(String courseName, RemoveCourseMaterialRequest removeCourseMaterialRequest) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        fileService.removeFile(removeCourseMaterialRequest.getCourseMaterialFile());
        final var courseMaterial = getCourseMaterialByName(course, removeCourseMaterialRequest.getCourseMaterialName());
        course.removeCourseMaterial(courseMaterial);
        courseRepository.save(course);
    }

    private CourseMaterial getCourseMaterialByName(Course course, String courseMaterialName) {
        return course.getCourseMaterials().stream()
                .filter(courseMaterial -> courseMaterial.getName().equals(courseMaterialName))
                .findFirst()
                .orElseThrow();
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.findByName(courseName).orElseThrow();
    }

    private void validateTeacherOwnsCourse(Course course) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (!course.getTeacher().equals(user)) {
            throw new IllegalStateException("Teacher does not own course");
        }
    }

    private File saveFile(AttachCourseMaterialRequest attachCourseMaterialRequest) {
        final var filename = Path.of(COURSE_MATERIALS_DIRECTORY, UUID.randomUUID() + "." + attachCourseMaterialRequest.getFileExtension()).toString();
        fileService.createDirectoryIfNotExists(COURSE_MATERIALS_DIRECTORY);
        return fileService.saveFile(filename, attachCourseMaterialRequest.getFileContent());
    }

}
