package com.mcube.FreightRateCalculator.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT response")
public class JwtAuthenticationResponse {

    @Schema(description = "accessToken")
    private String accessToken;
    @Schema(description = "refreshToken")
    private String refreshToken;
}
