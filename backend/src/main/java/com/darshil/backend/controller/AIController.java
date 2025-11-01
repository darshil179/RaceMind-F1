package com.darshil.backend.controller;

import com.darshil.backend.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RequiredArgsConstructor
@Slf4j
public class AIController {

    private final AIService aiService;

    @PostMapping("/insights")
    public ResponseEntity<Map<String, String>> getRaceInsight(
            @RequestBody Map<String, String> request) {

        String query = request.get("query");
        String context = request.getOrDefault("context", "");

        log.info("POST /api/ai/insights - Query: {}", query);

        String response = aiService.getRaceInsight(query, context);

        return ResponseEntity.ok(Map.of(
                "query", query,
                "response", response
        ));
    }

    @GetMapping("/driver-insight/{driverName}")
    public ResponseEntity<Map<String, String>> getDriverInsight(
            @PathVariable String driverName,
            @RequestParam(required = false, defaultValue = "0") int points,
            @RequestParam(required = false, defaultValue = "Unknown") String team) {

        log.info("GET /api/ai/driver-insight/{} - Generating insight", driverName);

        String insight = aiService.generateDriverInsight(driverName, points, team);

        return ResponseEntity.ok(Map.of(
                "driver", driverName,
                "insight", insight
        ));
    }
}