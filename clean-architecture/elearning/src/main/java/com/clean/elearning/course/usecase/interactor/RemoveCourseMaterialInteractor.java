package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.Course;
import com.clean.elearning.course.domain.CourseMaterial;
import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.RemoveCourseMaterialUseCase;
import com.clean.elearning.course.usecase.model.RemoveCourseMaterialModel;
import com.clean.elearning.course.usecase.port.FileRepository;
import com.clean.elearning.user.usecase.port.AuthContext;

public class RemoveCourseMaterialInteractor implements RemoveCourseMaterialUseCase {

    private final CourseRepository courseRepository;
    private final FileRepository fileRepository;
    private final AuthContext authContext;

    public RemoveCourseMaterialInteractor(CourseRepository courseRepository, FileRepository fileRepository, AuthContext authContext) {
        this.courseRepository = courseRepository;
        this.fileRepository = fileRepository;
        this.authContext = authContext;
    }

    @Override
    public void removeCourseMaterial(String courseName, RemoveCourseMaterialModel removeCourseMaterialModel) {
        final var course = getCourseByName(courseName);
        validateTeacherOwnsCourse(course);
        fileRepository.removeFile(removeCourseMaterialModel.getCourseMaterialFile());
        final var courseMaterial = getCourseMaterialByName(course, removeCourseMaterialModel.getCourseMaterialName());
        course.removeCourseMaterial(courseMaterial);
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

    private CourseMaterial getCourseMaterialByName(Course course, String courseMaterialName) {
        return course.getCourseMaterials().stream()
                .filter(courseMaterial -> courseMaterial.getName().equals(courseMaterialName))
                .findFirst()
                .orElseThrow();
    }

}
