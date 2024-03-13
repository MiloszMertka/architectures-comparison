package com.clean.elearning.course.adapter.ui.model;

import com.clean.elearning.course.domain.CourseMaterial;

import java.io.File;

public record CourseMaterialViewModel(
        String name,
        File file
) {

    public static CourseMaterialViewModel fromCourseMaterial(CourseMaterial courseMaterial) {
        return new CourseMaterialViewModel(
                courseMaterial.getName(),
                courseMaterial.getFile()
        );
    }

}
