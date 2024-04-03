package com.classic.elearning.course.infrastructure;

import com.classic.elearning.course.domain.*;
import com.classic.elearning.shared.seeder.Seeder;
import com.classic.elearning.user.domain.User;
import com.classic.elearning.user.domain.UserRepository;
import com.classic.elearning.user.infrastructure.UserSeeder;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(name = "seeder.enabled", havingValue = "true")
public class CourseSeeder implements Seeder {

    private static final int COURSES_TO_SEED = 5;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final Faker faker;
    private final UserSeeder userSeeder;

    @Override
    public void seed() {
        final Set<Course> courses = new HashSet<>();

        while (courses.size() < COURSES_TO_SEED) {
            final var course = createRandomCourse();

            if (!courses.contains(course)) {
                courses.add(course);
                courseRepository.save(course);
            }
        }
    }

    @Override
    public Set<Seeder> getDependencies() {
        return Set.of(userSeeder);
    }

    private Course createRandomCourse() {
        final var randomTeacher = getRandomTeacher();
        final var course = new Course(
                faker.lorem().sentence(),
                randomTeacher
        );
        final var studentsCount = faker.random().nextInt(1, 10);
        final var randomStudents = getRandomStudents(studentsCount);
        randomStudents.forEach(course::assignStudent);
        final var courseMaterials = createRandomCourseMaterials();
        courseMaterials.forEach(course::addCourseMaterial);
        final var quizzes = createRandomQuizzes();
        quizzes.forEach(course::addQuiz);
        return course;
    }

    private User getRandomTeacher() {
        final var teachers = userRepository.findAll().stream()
                .filter(User::isTeacher)
                .toList();
        final var randomIndex = faker.random().nextInt(0, teachers.size());
        return teachers.get(randomIndex);
    }

    private Set<User> getRandomStudents(int studentsCount) {
        final Set<User> randomStudents = new HashSet<>();
        final var students = userRepository.findAll().stream()
                .filter(User::isStudent)
                .toList();

        while (randomStudents.size() < studentsCount) {
            final var randomIndex = faker.random().nextInt(0, students.size() - 1);
            final var randomStudent = students.get(randomIndex);
            randomStudents.add(randomStudent);
        }

        return randomStudents;
    }

    private Set<CourseMaterial> createRandomCourseMaterials() {
        final Set<CourseMaterial> courseMaterials = new HashSet<>();
        final var courseMaterialsCount = faker.random().nextInt(1, 5);

        while (courseMaterials.size() < courseMaterialsCount) {
            final var courseMaterial = new CourseMaterial(
                    null,
                    faker.lorem().word(),
                    new File(faker.file().fileName()).getPath()
            );
            courseMaterials.add(courseMaterial);
        }

        return courseMaterials;
    }

    private List<Quiz> createRandomQuizzes() {
        final Set<Quiz> quizzes = new HashSet<>();
        final var quizzesCount = faker.random().nextInt(1, 5);

        while (quizzes.size() < quizzesCount) {
            final var quiz = new Quiz(
                    faker.lorem().sentence(),
                    Instant.now(),
                    Instant.now().plusSeconds(3600),
                    createRandomQuestions()
            );
            quizzes.add(quiz);
        }

        return new ArrayList<>(quizzes);
    }

    private List<Question> createRandomQuestions() {
        final List<Question> questions = new ArrayList<>();
        final var questionsCount = faker.random().nextInt(5, 10);

        while (questions.size() < questionsCount) {
            final var question = new Question(
                    null,
                    faker.lorem().sentence(),
                    faker.random().nextDouble() * 10,
                    createRandomAnswers()
            );
            questions.add(question);
        }

        return questions;
    }

    private List<Answer> createRandomAnswers() {
        final List<Answer> answers = new ArrayList<>();
        final var answersCount = faker.random().nextInt(2, 5);

        while (answers.size() < answersCount) {
            final var answer = new Answer(
                    null,
                    faker.lorem().sentence(),
                    false
            );
            answers.add(answer);
        }

        final var randomIndex = faker.random().nextInt(0, answers.size() - 1);
        final var correctAnswer = answers.get(randomIndex);
        answers.set(randomIndex, new Answer(null, correctAnswer.getContent(), true));

        return answers;
    }

}
