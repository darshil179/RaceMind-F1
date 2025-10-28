package com.darshil.backend.controller;

import com.darshil.backend.model.Driver;
import com.darshil.backend.repository.DriverRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "http://localhost:5173") // Vue frontend
public class DriverController {

    private final DriverRepository repo;
    public DriverController(DriverRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return repo.findAll();
    }

    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return repo.save(driver);
    }
}
