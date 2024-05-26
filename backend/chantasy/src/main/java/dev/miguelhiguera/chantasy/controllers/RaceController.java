package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.RaceDto;
import dev.miguelhiguera.chantasy.dtos.predictions.ResultDto;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.services.RaceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/races")
@RestController
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Race> getRace(@PathVariable Long id) {
        Optional<Race> optionalRace = raceService.getRace(id);

        if (optionalRace.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalRace.get());
    }

    @GetMapping("/next")
    public ResponseEntity<Race> getNextRace() {
        Optional<Race> optionalRace = raceService.getNextRace();
        return optionalRace.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Race>> allRaces() {
        return ResponseEntity.ok(raceService.allRaces());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Race> createRace(@Valid @RequestBody RaceDto input) {
        validateInput(input);
        return ResponseEntity.ok(raceService.createRace(input));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/{id}/results")
    public ResponseEntity<Void> addRaceResults(@PathVariable Long id, @Valid @RequestBody List<ResultDto> results) {
        raceService.addRaceResults(id, results);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Race> updateRace(@PathVariable Long id, @Valid @RequestBody RaceDto input) {
        validateInput(input);
        return ResponseEntity.ok(raceService.updateRace(id, input));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }

    private void validateInput(RaceDto input) {
        if (input.getPositionPredictionRangeStart() > input.getPositionPredictionRangeEnd()) {
            throw new IllegalArgumentException("Rango de predicción inválido. El inicio no puede ser mayor al final.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date startDate = dateFormat.parse(input.getPredictionStartDate());
            Date endDate = dateFormat.parse(input.getPredictionEndDate());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException("Fecha de inicio de predicción no puede ser mayor a la fecha de fin.");
            }

            if (startDate.equals(endDate)) {
                throw new IllegalArgumentException("Fecha de inicio de predicción no puede ser igual a la fecha de fin.");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Fecha de inicio de predicción inválida. Formato esperado: yyyy-MM-dd");
        }
    }
}
