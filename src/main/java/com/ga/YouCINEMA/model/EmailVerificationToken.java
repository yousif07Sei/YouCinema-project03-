package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {
    private Long id;

    private User user;

    private String token;

    private LocalDateTime expiresAt;
}
