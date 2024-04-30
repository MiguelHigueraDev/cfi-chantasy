package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.RegisterUserDto;
import dev.miguelhiguera.chantasy.entities.Role;
import dev.miguelhiguera.chantasy.entities.RoleEnum;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.repositories.RoleRepository;
import dev.miguelhiguera.chantasy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Admin role not found.");
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setDisplayName(input.getDisplayName());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());

        return userRepository.save(user);
    }
}
