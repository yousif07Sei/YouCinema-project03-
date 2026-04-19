package com.ga.YouCINEMA.controller;

import com.ga.YouCINEMA.dto.request.CinemaHallRequest;
import com.ga.YouCINEMA.dto.response.CinemaHallResponse;
import com.ga.YouCINEMA.service.CinemaHallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/halls")
public class CinemaHallController {

    private CinemaHallService cinemaHallService;

    @Autowired
    public void setCinemaHallService(CinemaHallService cinemaHallService) {
        this.cinemaHallService = cinemaHallService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CinemaHallResponse> createHall(
            @Valid @RequestBody CinemaHallRequest request) {
        return ResponseEntity.ok(cinemaHallService.createHall(request));
    }

    @GetMapping
    public ResponseEntity<List<CinemaHallResponse>> getAllHalls() {
        return ResponseEntity.ok(cinemaHallService.getAllHalls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaHallResponse> getHallById(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaHallService.getHallById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        cinemaHallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }



}
