package dev.miguelhiguera.chantasy.entities.predictions;

import dev.miguelhiguera.chantasy.entities.Race;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the "free" predictions that the users can make
 */

@Getter
@Setter
@Table(name = "free_predictions")
@Entity
public class FreePrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "race_id", referencedColumnName = "id", nullable = false)
    private Race race;

    @Column(nullable = false)
    private String prediction;
}
