package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.RegisterUserDto;
import dev.miguelhiguera.chantasy.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> allUsers(Pageable pageable);
    User createAdministrator(RegisterUserDto input);
}
