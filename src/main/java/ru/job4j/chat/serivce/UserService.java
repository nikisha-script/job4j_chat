package ru.job4j.chat.serivce;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.store.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findUserByLogin(String login) {
        return repository.findUserByLogin(login);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void update(User user) {
        repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(findUserById(id).get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByLogin(username).get();
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}
