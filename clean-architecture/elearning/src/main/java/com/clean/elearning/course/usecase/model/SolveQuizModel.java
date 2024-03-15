package com.clean.elearning.course.usecase.model;

import java.util.Map;

public interface SolveQuizModel {

    String getQuizName();

    Map<String, String> getAnsweredQuestions();

}
