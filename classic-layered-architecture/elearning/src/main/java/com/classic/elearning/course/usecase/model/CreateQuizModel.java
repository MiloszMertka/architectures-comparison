package com.classic.elearning.course.usecase.model;

import com.classic.elearning.course.domain.Question;

import java.time.LocalDateTime;
import java.util.List;

public interface CreateQuizModel {

    String getName();

    LocalDateTime getOpeningTime();

    LocalDateTime getClosingTime();

    List<Question> getQuestions();

}
