package com.darshil.backend.controller;

import com.darshil.backend.service.F1SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/f1")
@RequiredArgsConstructor
public class F1ApiController {

    private final F1SyncService syncService;

    @GetMapping("/sync/all")
    public String syncAllData() {
        syncService.syncSeasons(List.of(2024, 2025), 25);
        return "✅ F1 Drivers, Races, and Teams synced successfully!";
    }
}
