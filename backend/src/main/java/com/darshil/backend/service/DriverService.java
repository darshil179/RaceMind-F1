package com.darshil.backend.service;

import com.darshil.backend.model.Driver;
import com.darshil.backend.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository driverRepository;
    private final ErgastService ergastService;

    @Cacheable("drivers")
    public List<Driver> getAllDrivers() {
        log.info("Fetching all drivers");
        return driverRepository.findAllByOrderByPositionAsc();
    }

    @Cacheable(value = "driver", key = "#id")
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    @Cacheable("topDrivers")
    public List<Driver> getTopDrivers() {
        log.info("Fetching top 10 drivers");
        return driverRepository.findTop10ByOrderByPointsDesc();
    }

    @Transactional
    @CacheEvict(value = {"drivers", "topDrivers"}, allEntries = true)
    public void syncDriverStandings() {
        log.info("Syncing driver standings from Ergast API");

        int currentSeason = LocalDate.now().getYear();
        List<Driver> apiDrivers = ergastService.getDriverStandings(currentSeason);

        for (Driver apiDriver : apiDrivers) {
            Driver existingDriver = driverRepository.findByDriverId(apiDriver.getDriverId())
                    .orElse(new Driver());

            // Update driver information
            existingDriver.setDriverId(apiDriver.getDriverId());
            existingDriver.setFirstName(apiDriver.getFirstName());
            existingDriver.setLastName(apiDriver.getLastName());
            existingDriver.setFullName(apiDriver.getFullName());
            existingDriver.setTeam(apiDriver.getTeam());
            existingDriver.setTeamShort(apiDriver.getTeamShort());
            existingDriver.setPoints(apiDriver.getPoints());
            existingDriver.setPosition(apiDriver.getPosition());
            existingDriver.setWins(apiDriver.getWins());
            existingDriver.setLastUpdated(LocalDate.now());

            driverRepository.save(existingDriver);
        }

        log.info("Driver standings synced successfully");
    }

    @Transactional
    public Driver createOrUpdateDriver(Driver driver) {
        driver.setLastUpdated(LocalDate.now());
        return driverRepository.save(driver);
    }
}