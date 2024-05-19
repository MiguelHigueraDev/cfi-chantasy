package dev.miguelhiguera.chantasy.entities.predictions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Race;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the results in every race, manually entered by the admins.
 */

@Getter
@Setter
@Table(name = "results")
@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "race_id", referencedColumnName = "id", nullable = false)
    private Race race;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    private Driver driver;

    @Column(nullable = false)
    private Short position;

    @Column(nullable = false)
    private Boolean didFinish;
}
