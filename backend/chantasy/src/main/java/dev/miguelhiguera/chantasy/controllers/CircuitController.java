package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.services.CircuitService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping("/api/circuits")
@RestController
public class CircuitController {

    private final CircuitService circuitService;

    public CircuitController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Circuit> getCircuit(@PathVariable Long id) {
        Optional<Circuit> optionalCircuit = circuitService.getCircuit(id);

        if (optionalCircuit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalCircuit.get());
    }

    @GetMapping("/")
    public ResponseEntity<Page<Circuit>> allCircuits(Pageable pageable) {
        Page<Circuit> circuits = circuitService.allCircuits(pageable);
        return ResponseEntity.ok(circuits);
    }

    @PostMapping("/")
    public ResponseEntity<Circuit> createCircuit(@Valid @RequestBody CircuitDto input) {
        return ResponseEntity.ok(circuitService.createCircuit(input));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Circuit> updateCircuit(@PathVariable Long id, @Valid @RequestBody CircuitDto input) {
        return ResponseEntity.ok(circuitService.updateCircuit(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuit(@PathVariable Long id) {
        circuitService.deleteCircuit(id);
        return ResponseEntity.noContent().build();
    }
}
