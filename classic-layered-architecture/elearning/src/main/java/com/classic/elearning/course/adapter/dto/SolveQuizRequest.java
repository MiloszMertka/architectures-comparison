package com.classic.elearning.course.adapter.dto;

import com.classic.elearning.course.usecase.model.SolveQuizModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SolveQuizRequest implements SolveQuizModel {

    private String quizName;
    private Map<String, String> answeredQuestions;

}
