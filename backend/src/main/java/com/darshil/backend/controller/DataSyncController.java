package com.darshil.backend.controller;

import com.darshil.backend.service.ErgastF1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // adjust if needed
public class DataSyncController {

    private final ErgastF1Service ergastF1Service;

    @GetMapping
    public ResponseEntity<String> syncNow() {
        ergastF1Service.fetchAndSaveRaces();
        ergastF1Service.fetchAndSaveDrivers();
        return ResponseEntity.ok("✅ F1 data synced manually!");
    }
}
