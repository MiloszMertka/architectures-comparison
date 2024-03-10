package com.clean.elearning.user.adapter.dto;

import com.clean.elearning.user.usecase.model.ChangePasswordModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest implements ChangePasswordModel {

    @NotBlank
    @Size(min = 8, max = 255)
    private String currentPassword;
    
    @NotBlank
    @Size(min = 8, max = 255)
    private String newPassword;

}
