package com.darshil.backend.config;

import com.darshil.backend.service.F1SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final F1SyncService syncService;

    // run every day at 00:00 server time
    @Scheduled(cron = "0 0 0 * * ?")
    public void nightlySync() {
        syncService.syncSeasons(List.of(2024, 2025), 25);
    }
}
