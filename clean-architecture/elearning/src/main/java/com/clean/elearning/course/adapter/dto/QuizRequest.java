package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.domain.Question;

import java.time.LocalDateTime;
import java.util.List;

public interface QuizRequest {

    String getName();

    LocalDateTime getOpeningTime();

    LocalDateTime getClosingTime();

    List<Question> getQuestions();

}
