package com.darshil.backend.controller;

import com.darshil.backend.model.Driver;
import com.darshil.backend.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fetch")
@RequiredArgsConstructor
public class ErgastController {
    private final ErgastF1Service ergastF1Service;

    @GetMapping("/races")
    public String fetchRaces() {
        ergastF1Service.fetchAndSaveRaces();
        return "✅ F1 data synced manually!";
    }

    @GetMapping("/drivers")
    public String fetchDrivers() {
        ergastF1Service.fetchAndSaveDrivers();
        return "✅ Drivers synced manually!";
    }
}
