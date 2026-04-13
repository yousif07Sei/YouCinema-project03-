package com.ga.YouCINEMA.dto.response;

import lombok.*;

/**
 * DTO for authenticated user response.
 * Returned to the client after successful login or registration.
 * Contains the JWT token and basic user info.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticatedUserResponse {

    private String token;
    private String email;
    private String role;
    private String message;
}