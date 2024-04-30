package dev.miguelhiguera.chantasy.bootstrap;

import dev.miguelhiguera.chantasy.entities.Role;
import dev.miguelhiguera.chantasy.entities.RoleEnum;
import dev.miguelhiguera.chantasy.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * This automatically seeds the database with predefined roles in case they don't already exist.
 */

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] {RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN};
        Map<RoleEnum, String> roleDescriptions = Map.of(
                RoleEnum.USER, "Usuario normal, puede participar de las predicciones.",
                RoleEnum.ADMIN, "Usuario administrador, puede crear usuarios y carreras, definiendo los puntos otorgados, además de revisar las respuestas.",
                RoleEnum.SUPER_ADMIN, "Usuario super administrador, puede crear usuarios administrador y realizar las tareas de administrador."
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();

                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptions.get(roleName));
                roleRepository.save(roleToCreate);
            });
        });
    }
}
