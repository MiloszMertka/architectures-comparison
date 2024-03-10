package com.clean.elearning.course.infrastructure;

import com.clean.elearning.course.domain.CourseRepository;
import com.clean.elearning.course.usecase.*;
import com.clean.elearning.course.usecase.interactor.*;
import com.clean.elearning.user.domain.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CourseConfig {

    @Bean
    public BrowseCoursesUseCase browseCoursesUseCase(CourseRepository courseRepository) {
        return new BrowseCoursesInteractor(courseRepository);
    }

    @Bean
    public CreateCourseUseCase createCourseUseCase(CourseRepository courseRepository, UserRepository userRepository) {
        return new CreateCourseInteractor(courseRepository, userRepository);
    }

    @Bean
    public UpdateCourseUseCase updateCourseUseCase(CourseRepository courseRepository, UserRepository userRepository) {
        return new UpdateCourseInteractor(courseRepository, userRepository);
    }

    @Bean
    public DeleteCourseUseCase deleteCourseUseCase(CourseRepository courseRepository) {
        return new DeleteCourseInteractor(courseRepository);
    }

    @Bean
    public AssignStudentsToCourseUseCase assignStudentsToCourseUseCase(CourseRepository courseRepository, UserRepository userRepository) {
        return new AssignStudentsToCourseInteractor(courseRepository, userRepository);
    }

    @Bean
    public RemoveStudentsFromCourseUseCase removeStudentsFromCourseUseCase(CourseRepository courseRepository, UserRepository userRepository) {
        return new RemoveStudentsFromCourseInteractor(courseRepository, userRepository);
    }

}
