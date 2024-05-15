package dev.miguelhiguera.chantasy.entities.predictions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.miguelhiguera.chantasy.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the answers to the questions that the users can answer.
 */

@Getter
@Setter
@Table(name = "answers")
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String answer;

    @Column
    private boolean isCorrect;

    @JsonProperty("userId")
    public Long getUserId() {
        return user.getId();
    }
}
