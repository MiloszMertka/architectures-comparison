package com.classic.elearning.course.adapter.dto;

import com.classic.elearning.course.usecase.model.AttachCourseMaterialModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachCourseMaterialRequest implements AttachCourseMaterialModel {

    @NotBlank
    @Size(max = 255)
    private String name;
    private byte[] fileContent;
    private String fileExtension;

}
