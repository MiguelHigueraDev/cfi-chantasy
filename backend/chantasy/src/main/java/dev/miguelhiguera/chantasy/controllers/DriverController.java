package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.DriverDto;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.services.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping("/api/drivers")
@RestController
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriver(@PathVariable Long id) {
        Optional<Driver> optionalDriver = driverService.getDriver(id);

        if (optionalDriver.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalDriver.get());
    }

    @GetMapping("/")
    public ResponseEntity<List<Driver>> allDrivers() {
        return ResponseEntity.ok(driverService.allDrivers());
    }

    @PostMapping("/")
    public ResponseEntity<Driver> createDriver(@Valid @RequestBody DriverDto input) {
        return ResponseEntity.ok(driverService.createDriver(input));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long id, @Valid @RequestBody DriverDto input) {
        return ResponseEntity.ok(driverService.updateDriver(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}
