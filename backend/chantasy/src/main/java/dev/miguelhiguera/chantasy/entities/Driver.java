package dev.miguelhiguera.chantasy.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Table(name = "drivers")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "team_id", referencedColumnName = "id", nullable = false)
    private Team team;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
