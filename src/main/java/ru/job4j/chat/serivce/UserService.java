package ru.job4j.chat.serivce;

import org.springframework.stereotype.Service;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.store.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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
}
