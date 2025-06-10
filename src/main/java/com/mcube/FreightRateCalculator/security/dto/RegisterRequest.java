package com.mcube.FreightRateCalculator.security.dto;

import com.mcube.FreightRateCalculator.security.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "Register request")
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Schema(description = "Username")
    @NotBlank(message = "Username is required")
    @Size(min=5, max=50, message="Username must contains from 5 to 50 characters")
    private String username;

    @Schema(description = "Password")
    @NotBlank(message = "Password is required")
    @Size(max=50, message="Password must contains no more than 50 characters")
    private String password;

    @Schema(description = "firstname")
    private String firstname;

    @Schema(description = "lastname")
    private String lastname;

    @Schema(description = "Roles")
    private List<Role> roles;

}
