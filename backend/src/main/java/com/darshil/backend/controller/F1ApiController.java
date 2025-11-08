package com.darshil.backend.controller;

import com.darshil.backend.service.F1ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/f1")
@RequiredArgsConstructor
public class F1ApiController {

    private final F1ApiService f1ApiService;

    @GetMapping("/sync")
    public String syncAllData() {
        f1ApiService.syncAll();
        return "✅ F1 Drivers and Race synced successfully!";
    }
}
