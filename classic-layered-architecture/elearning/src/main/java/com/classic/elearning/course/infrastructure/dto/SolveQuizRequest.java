package com.classic.elearning.course.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SolveQuizRequest {

    private String quizName;
    private Map<String, String> answeredQuestions;

}
