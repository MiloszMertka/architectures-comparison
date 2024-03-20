package com.clean.elearning.course.usecase.model;

import com.clean.elearning.course.domain.Question;

import java.time.LocalDateTime;
import java.util.List;

public interface CreateQuizModel {

    String getName();

    LocalDateTime getOpeningTime();

    LocalDateTime getClosingTime();

    List<Question> getQuestions();

}
