package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.*;

import com.ga.YouCINEMA.dto.response.AuthenticatedUserResponse;
import com.ga.YouCINEMA.enums.UserRole;
import com.ga.YouCINEMA.enums.UserStatus;
import com.ga.YouCINEMA.model.EmailVerificationToken;
import com.ga.YouCINEMA.model.PasswordResetToken;
import com.ga.YouCINEMA.model.User;
import com.ga.YouCINEMA.repository.EmailVerificationTokenRepository;
import com.ga.YouCINEMA.repository.PasswordResetTokenRepository;
import com.ga.YouCINEMA.repository.UserRepository;
import com.ga.YouCINEMA.util.EmailUtils;
import com.ga.YouCINEMA.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthenticatedUserResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Build and save the new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_CUSTOMER)
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .build();

        userRepository.save(user);

        String tokenValue = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(tokenValue)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        // Send verification email
        emailUtils.sendVerificationEmail(user.getEmail(), tokenValue);

        return AuthenticatedUserResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Registration successful. Please verify your email.")
                .build();
    }


    public AuthenticatedUserResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check email is verified
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        // Check account is active
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("Your account has been deactivated");
        }

        // Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtils.generateToken(userDetails);

        return AuthenticatedUserResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login successful")
                .build();
    }

    public AuthenticatedUserResponse verifyEmail(String token) {

        // Find the token
        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        // Check if token is expired
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        // Mark user as verified
        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        // Delete the used token
        emailVerificationTokenRepository.deleteByUserId(user.getId());

        return AuthenticatedUserResponse.builder()
                .email(user.getEmail())
                .message("Email verified successfully. You can now log in.")
                .build();
    }

    public AuthenticatedUserResponse forgotPassword(ForgotPasswordRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No account found with that email"));

        // Delete any existing reset tokens for this user
        passwordResetTokenRepository.deleteByUserId(user.getId());

        // Generate reset token
        String tokenValue = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(tokenValue)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);

        // Send reset email
        emailUtils.sendPasswordResetEmail(user.getEmail(), tokenValue);

        return AuthenticatedUserResponse.builder()
                .message("Password reset link sent to your email.")
                .build();
    }
    public AuthenticatedUserResponse resetPassword(ResetPasswordRequest request) {

        // Find the token
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        // Check if token is expired
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        // Update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Delete the used token
        passwordResetTokenRepository.deleteByUserId(user.getId());

        return AuthenticatedUserResponse.builder()
                .email(user.getEmail())
                .message("Password reset successfully. You can now log in.")
                .build();
    }
    public AuthenticatedUserResponse changePassword(String email, ChangePasswordRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return AuthenticatedUserResponse.builder()
                .email(user.getEmail())
                .message("Password changed successfully.")
                .build();
    }

}