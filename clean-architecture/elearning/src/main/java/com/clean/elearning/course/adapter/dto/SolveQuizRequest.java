package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.usecase.model.SolveQuizModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SolveQuizRequest implements SolveQuizModel {

    private String quizName;
    private Map<String, String> answeredQuestions;

}
