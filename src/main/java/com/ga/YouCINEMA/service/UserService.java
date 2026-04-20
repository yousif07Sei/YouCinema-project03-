package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.UpdateProfileRequest;
import com.ga.YouCINEMA.dto.response.UserResponse;
import com.ga.YouCINEMA.enums.UserStatus;
import com.ga.YouCINEMA.model.User;
import com.ga.YouCINEMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ga.YouCINEMA.exception.InformationNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UserService {

    private UserRepository userRepository;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    public UserResponse uploadProfilePicture(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        try {
            // Create uploads directory if it doesn't exist
            String uploadDir = "uploads/profiles/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Update user profile picture
            user.setProfilePicture(uploadDir + fileName);
            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
        }

        return mapToUserResponse(user);
    }

    public UserResponse softDeleteAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        user.setStatus(UserStatus.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .profilePicture(user.getProfilePicture())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
