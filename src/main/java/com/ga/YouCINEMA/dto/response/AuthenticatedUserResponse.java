package com.ga.YouCINEMA.dto.response;

import lombok.*;

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