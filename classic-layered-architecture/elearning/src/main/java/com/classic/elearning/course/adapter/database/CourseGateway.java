package com.classic.elearning.course.adapter.database;

import com.classic.elearning.course.adapter.database.CourseSchema;
import com.classic.elearning.course.adapter.database.CourseSchemaRepository;
import com.classic.elearning.course.domain.Course;
import com.classic.elearning.course.domain.CourseRepository;
import com.classic.elearning.user.adapter.database.UserSchema;
import com.classic.elearning.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CourseGateway implements CourseRepository {

    private final CourseSchemaRepository courseSchemaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseSchemaRepository.findAll().stream()
                .map(com.classic.elearning.course.adapter.database.CourseSchema::toCourse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> searchCourses(@NonNull String searchText) {
        return courseSchemaRepository.search(searchText).stream()
                .map(com.classic.elearning.course.adapter.database.CourseSchema::toCourse)
                .toList();
    }

    @Override
    public List<Course> getUserCourses(@NonNull User user) {
        final var userSchema = UserSchema.fromUser(user);
        return courseSchemaRepository.findAllByUser(userSchema).stream()
                .map(com.classic.elearning.course.adapter.database.CourseSchema::toCourse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseByName(@NonNull String name) {
        return courseSchemaRepository.findByName(name)
                .map(com.classic.elearning.course.adapter.database.CourseSchema::toCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean courseExistsByName(@NonNull String name) {
        return courseSchemaRepository.existsByName(name);
    }

    @Override
    @Transactional
    public void saveCourse(@NonNull Course course) {
        final var courseSchema = CourseSchema.fromCourse(course);
        courseSchemaRepository.save(courseSchema);
    }

    @Override
    @Transactional
    public void deleteCourseByName(@NonNull String name) {
        courseSchemaRepository.deleteByName(name);
    }

}
