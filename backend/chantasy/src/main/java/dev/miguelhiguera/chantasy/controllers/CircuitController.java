package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.services.CircuitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping("/api/circuits")
@RestController
public class CircuitController {

    private final CircuitService circuitService;

    public CircuitController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Circuit>> allCircuits() {
        return ResponseEntity.ok(circuitService.allCircuits());
    }

    @PostMapping("/")
    public ResponseEntity<Circuit> createCircuit(@RequestBody CircuitDto input) {
        return ResponseEntity.ok(circuitService.createCircuit(input));
    }
}
