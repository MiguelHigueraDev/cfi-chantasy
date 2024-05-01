package dev.miguelhiguera.chantasy.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "races")
@Entity
@Builder
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
    private boolean isQualifier;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Race() {

    }
}
