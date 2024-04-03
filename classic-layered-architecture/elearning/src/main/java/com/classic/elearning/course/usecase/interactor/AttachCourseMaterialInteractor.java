package com.classic.elearning.course.usecase.interactor;

import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseMaterial;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.usecase.AttachCourseMaterialUseCase;
import com.classic.elearning.course.usecase.model.AttachCourseMaterialModel;
import com.classic.elearning.course.usecase.port.FileRepository;
import com.classic.elearning.user.usecase.port.AuthContext;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public class AttachCourseMaterialInteractor implements AttachCourseMaterialUseCase {

    private static final String COURSE_MATERIALS_DIRECTORY = "course_materials";
    private final CourseRepository courseRepository;
    private final FileRepository fileRepository;
    private final AuthContext authContext;

    public AttachCourseMaterialInteractor(CourseRepository courseRepository, FileRepository fileRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.fileRepository = fileRepository;
        this.authContext = authContext;
    }

    @Override
    public void attachCourseMaterial(String courseName, AttachCourseMaterialModel attachCourseMaterialModel) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        final var savedFile = saveFile(attachCourseMaterialModel);
        final var courseMaterial = new CourseMaterial(attachCourseMaterialModel.getName(), savedFile);
        course.addCourseMaterial(courseMaterial);
        courseRepository.saveCourse(course);
    }

    private Course getCourseByName(String courseName) {
        return courseRepository.getCourseByName(courseName).orElseThrow();
    }

    private void validateTeacherOwnsCourse(Course course) {
        if (!course.getTeacher().equals(authContext.getCurrentUser())) {
            throw new IllegalStateException("Teacher does not own course");
        }
    }

    private File saveFile(AttachCourseMaterialModel attachCourseMaterialModel) {
        final var filename = Path.of(COURSE_MATERIALS_DIRECTORY, UUID.randomUUID() + "." + attachCourseMaterialModel.getFileExtension()).toString();
        fileRepository.createDirectoryIfNotExists(COURSE_MATERIALS_DIRECTORY);
        return fileRepository.saveFile(filename, attachCourseMaterialModel.getFileContent());
    }

}
