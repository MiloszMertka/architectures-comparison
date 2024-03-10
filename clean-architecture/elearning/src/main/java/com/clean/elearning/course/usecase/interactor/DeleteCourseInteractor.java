package com.clean.elearning.course.usecase.interactor;

import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.DeleteCourseUseCase;

public class DeleteCourseInteractor implements DeleteCourseUseCase {

    private final CourseRepository courseRepository;

    public DeleteCourseInteractor(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    @Override
    public void deleteCourse(String name) {
        validateCourseExists(name);
        courseRepository.deleteCourseByName(name);
    }

    private void validateCourseExists(String name) {
        if (!courseRepository.courseExistsByName(name)) {
            throw new IllegalStateException("Course with name " + name + " does not exist");
        }
    }

}
