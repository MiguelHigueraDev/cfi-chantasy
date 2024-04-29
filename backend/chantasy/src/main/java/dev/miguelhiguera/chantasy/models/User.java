package dev.miguelhiguera.chantasy.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String displayName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
