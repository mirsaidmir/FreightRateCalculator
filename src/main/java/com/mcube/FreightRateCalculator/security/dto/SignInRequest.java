package com.mcube.FreightRateCalculator.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Sign in request")
public class SignInRequest {

    @Schema(description = "Username")
    @NotBlank(message = "Username is required")
    @Size(min=5, max=50, message="Username must contains from 5 to 50 characters")
    private String username;

    @Schema(description = "Password")
    @NotBlank(message = "Password is required")
    @Size(max=50, message="Password must contains no more than 50 characters")
    private String password;
}
