package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.exception.UserByLoginExistsException;
import ru.job4j.chat.exception.UserNotFoundException;
import ru.job4j.chat.model.UserDto;
import ru.job4j.chat.serivce.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final BCryptPasswordEncoder encoder;

    public UserController(UserService service, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        return UserDto.getUserDto(checkExistsUser(id).get());
    }

    @PostMapping()
    public ResponseEntity<User> save(@RequestBody User user) {
        Optional<User> tempUser = service.findUserByLogin(user.getLogin());
        if (tempUser.isPresent()) {
            throw new UserByLoginExistsException("user with this login already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return new ResponseEntity<>(
                service.save(user),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {
        Optional<User> rsl = checkExistsUser(user.getId());
        service.update(rsl.get());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional<User> user = checkExistsUser(id);
        service.delete(user.get().getId());
        return ResponseEntity.ok().build();
    }

    private Optional<User> checkExistsUser(Long id) {
        Optional<User> user = service.findUserById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Not found user by id");
        }
        return user;
    }
}
