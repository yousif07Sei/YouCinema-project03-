package com.ga.YouCINEMA.controller;

import com.ga.YouCINEMA.dto.request.UpdateProfileRequest;
import com.ga.YouCINEMA.dto.response.UserResponse;
import com.ga.YouCINEMA.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Principal principal) {
        return ResponseEntity.ok(userService.getMyProfile(principal.getName()));
    }


    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Principal principal) {
        return ResponseEntity.ok(userService.updateProfile(principal.getName(), request));
    }


    @PostMapping("/me/profile-picture")
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        return ResponseEntity.ok(userService.uploadProfilePicture(principal.getName(), file));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> softDeleteAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(userService.softDeleteAdmin(id));
    }
}