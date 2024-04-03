package com.classic.elearning.course.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachCourseMaterialRequest {

    @NotBlank
    @Size(max = 255)
    private String name;
    private byte[] fileContent;
    private String fileExtension;

}
