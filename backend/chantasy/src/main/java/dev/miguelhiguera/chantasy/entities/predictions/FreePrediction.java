package dev.miguelhiguera.chantasy.entities.predictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.User;
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @JsonProperty("userId")
    public Long getUserId() {
        return user.getId();
    }
}
