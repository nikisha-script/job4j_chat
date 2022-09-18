package ru.job4j.chat.serivce;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.exception.UserByLoginExistsException;
import ru.job4j.chat.store.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
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

    public void patch(User user) throws InvocationTargetException, IllegalAccessException {
        var current = repository.findById(user.getId());
        if (current.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person to update not found");
        }
        var methods = current.get().getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : " + current + ", Check set and get pairs.");
                }
                if (getMethod.getName().equals("getName")) {
                    String newValue = (String) getMethod.invoke(user);
                    if (newValue != null) {
                        var personDb = repository.findUserByLogin(newValue);
                        if (personDb.isPresent()) {
                            throw new UserByLoginExistsException(newValue + " this name already exists");
                        }
                        setMethod.invoke(current.get(), newValue);
                    }
                }
                var newValue = getMethod.invoke(user);
                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        repository.save(current.get());
    }
}
