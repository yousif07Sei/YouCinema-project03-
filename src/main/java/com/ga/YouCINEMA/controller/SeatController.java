package com.ga.YouCINEMA.controller;

import com.ga.YouCINEMA.dto.request.UpdateSeatTypeRequest;
import com.ga.YouCINEMA.dto.response.SeatResponse;
import com.ga.YouCINEMA.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SeatController {

    private SeatService seatService;

    @GetMapping("/api/halls/{hallId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeatsByHall(@PathVariable Long hallId) {
        return ResponseEntity.ok(seatService.getSeatsByHall(hallId));
    }

    @GetMapping("/api/showtimes/{showtimeId}/seats/available")
    public ResponseEntity<List<SeatResponse>> getAvailableSeatsByShowtime(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(seatService.getAvailableSeatsByShowtime(showtimeId));
    }

    @PutMapping("/api/seats/{id}/type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SeatResponse> updateSeatType(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatTypeRequest request) {
        return ResponseEntity.ok(seatService.updateSeatType(id, request));
    }

}
