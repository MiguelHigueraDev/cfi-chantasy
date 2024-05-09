package dev.miguelhiguera.chantasy.entities.predictions;

import dev.miguelhiguera.chantasy.entities.Race;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Represents the questions that the users can answer for each race.
 */

@Getter
@Setter
@Table(name = "questions")
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "race_id", referencedColumnName = "id", nullable = false)
    private Race race;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private Short points;

    @OneToMany(mappedBy = "question")
    private Set<Answer> answers;
}
