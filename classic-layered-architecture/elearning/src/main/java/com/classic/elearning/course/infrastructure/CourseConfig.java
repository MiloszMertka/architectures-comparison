package com.classic.elearning.course.infrastructure;

import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.course.usecase.*;
import com.classic.elearning.course.usecase.interactor.*;
import com.classic.elearning.course.usecase.port.FileRepository;
import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.service.port.AuthContext;
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
    public BrowseUserCoursesUseCase browseUserCoursesUseCase(CourseRepository courseRepository, AuthContext authContext) {
        return new BrowseUserCoursesInteractor(courseRepository, authContext);
    }

    @Bean
    public ViewCourseUseCase viewCourseUseCase(CourseRepository courseRepository) {
        return new ViewCourseInteractor(courseRepository);
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

    @Bean
    public AttachCourseMaterialUseCase attachCourseMaterialUseCase(CourseRepository courseRepository, FileRepository fileRepository, AuthContext authContext) {
        return new AttachCourseMaterialInteractor(courseRepository, fileRepository, authContext);
    }

    @Bean
    public RemoveCourseMaterialUseCase removeCourseMaterialUseCase(CourseRepository courseRepository, FileRepository fileRepository, AuthContext authContext) {
        return new RemoveCourseMaterialInteractor(courseRepository, fileRepository, authContext);
    }

    @Bean
    public CreateQuizUseCase createQuizUseCase(CourseRepository courseRepository, AuthContext authContext) {
        return new CreateQuizInteractor(courseRepository, authContext);
    }

    @Bean
    public UpdateQuizUseCase updateQuizUseCase(CourseRepository courseRepository, AuthContext authContext) {
        return new UpdateQuizInteractor(courseRepository, authContext);
    }

    @Bean
    public DeleteQuizUseCase deleteQuizUseCase(CourseRepository courseRepository, AuthContext authContext) {
        return new DeleteQuizInteractor(courseRepository, authContext);
    }

    @Bean
    public SolveQuizUseCase solveQuizUseCase(CourseRepository courseRepository, AuthContext authContext) {
        return new SolveQuizInteractor(courseRepository, authContext);
    }

    @Bean
    public BrowseQuizResultsUseCase browseQuizResults(CourseRepository courseRepository, AuthContext authContext) {
        return new BrowseQuizResultsInteractor(courseRepository, authContext);
    }

}
