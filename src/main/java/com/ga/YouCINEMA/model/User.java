package com.ga.YouCINEMA.model;

import com.ga.YouCINEMA.enums.UserRole;
import com.ga.YouCINEMA.enums.UserStatus;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private  String password;

    private UserRole role;

    private UserStatus status;

    private String profilePicture;

    private boolean emailVerified = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

}
