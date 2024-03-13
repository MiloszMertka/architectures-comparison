package com.clean.elearning.course.adapter.dto;

import com.clean.elearning.course.usecase.model.RemoveCourseMaterialModel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class RemoveCourseMaterialRequest implements RemoveCourseMaterialModel {

    String courseMaterialName;
    File courseMaterialFile;

}
