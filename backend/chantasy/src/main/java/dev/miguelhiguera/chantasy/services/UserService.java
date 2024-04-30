package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.RegisterUserDto;
import dev.miguelhiguera.chantasy.entities.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();
    User createAdministrator(RegisterUserDto input);
}
