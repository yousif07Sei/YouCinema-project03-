package com.ga.YouCINEMA.model;

import java.time.LocalDateTime;

public class EmailVerificationToken {
    private Long id;

    private User user;

    private String token;

    private LocalDateTime expiresAt;
}
