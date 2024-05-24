package dev.miguelhiguera.chantasy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.miguelhiguera.chantasy.entities.predictions.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Table(name = "races")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "circuit_id", referencedColumnName = "id", nullable = false)
    private Circuit circuit;

    @Column(nullable = false)
    private Short maxDnfAwarded;

    /**
     * These determine the range of positions that award points if predicted correctly.
     */

    @Column(nullable = false)
    private Short positionPredictionRangeStart;

    @Column(nullable = false)
    private Short positionPredictionRangeEnd;

    @Column(nullable = false)
    private Short dnfPoints;

    @Column(nullable = false)
    private Short positionPoints;

    @Column(nullable = false)
    private LocalDateTime predictionStartDate;

    @Column(nullable = false)
    private LocalDateTime predictionEndDate;

    @Column(nullable = false)
    private Short maxFreePredictions;

    @Column(nullable = false)
    private boolean isQualifier;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    /**
     * All the questions that the users can answer.
     */

    @JsonIgnore
    @OneToMany(mappedBy = "race")
    private Set<Question> questions;

    /**
     * All the free predictions that users have made.
     */

    @OneToMany(mappedBy = "race")
    private Set<FreePrediction> freePredictions;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Driver> drivers;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Result> results;
}
