package com.classic.elearning.course.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class RemoveCourseMaterialRequest {

    String courseMaterialName;
    File courseMaterialFile;

}
