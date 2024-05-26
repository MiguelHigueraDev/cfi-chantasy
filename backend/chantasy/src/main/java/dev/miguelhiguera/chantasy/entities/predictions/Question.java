package dev.miguelhiguera.chantasy.entities.predictions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.miguelhiguera.chantasy.entities.Race;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Represents the questions that the users can answer for each race.
 */

@Getter
@Setter
@Table(name = "questions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "race_id", referencedColumnName = "id", nullable = false)
    private Race race;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private Short points;

    @OneToMany
    private Set<Answer> answers;
}
