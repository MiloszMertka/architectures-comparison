package com.clean.elearning.user.adapter.dto;

import com.clean.elearning.user.domain.Role;
import com.clean.elearning.user.usecase.model.CreateUserModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest implements CreateUserModel {

    private @NotBlank @Size(max = 255) String firstName;
    private @NotBlank @Size(max = 255) String lastName;
    private @NotBlank @Size(max = 255) @Email String email;
    private @NotBlank @Size(min = 8, max = 255) String password;
    private @NotNull Role role;

}
