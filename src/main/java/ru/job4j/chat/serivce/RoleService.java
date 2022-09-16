package ru.job4j.chat.serivce;

import org.springframework.stereotype.Service;
import ru.job4j.chat.entity.Role;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.store.RoleRepository;
import ru.job4j.chat.store.UserRepository;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Optional<Role> findAllByName(String name) {
        return roleRepository.findAllByName(name);
    }

    public Role save(Role role, Long id) {
        User user = userRepository.findById(id).get();
        role.setUser(user);
        return roleRepository.save(role);
    }

}
