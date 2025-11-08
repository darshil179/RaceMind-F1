package com.darshil.backend.config;

import com.darshil.backend.service.ErgastF1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final ErgastF1Service ergastF1Service;

    // Run every day at midnight (server time)
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDataNightly() {
        System.out.println("🌙 Running F1 data sync...");
        ergastF1Service.fetchAndSaveRaces();
        ergastF1Service.fetchAndSaveDrivers();
    }
}
