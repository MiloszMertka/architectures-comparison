package com.classic.elearning.course.adapter.dto;

import com.classic.elearning.course.usecase.model.RemoveCourseMaterialModel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class RemoveCourseMaterialRequest implements RemoveCourseMaterialModel {

    String courseMaterialName;
    File courseMaterialFile;

}
