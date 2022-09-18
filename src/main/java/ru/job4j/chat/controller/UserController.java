package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.exception.UserByLoginExistsException;
import ru.job4j.chat.exception.UserNotFoundException;
import ru.job4j.chat.serivce.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public UserController(UserService service,
                          BCryptPasswordEncoder encoder,
                          ObjectMapper objectMapper) {
        this.service = service;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Optional<User> findById(@PathVariable("id") Long id) {
        return Optional.ofNullable(checkExistsUser(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
        )));
    }

    @PostMapping()
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
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

    @PostMapping("/auth")
    public @ResponseBody User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        User user = (principal instanceof User) ? (User) principal : null;
        return Objects.nonNull(user) ? this.service.findUserByLogin(user.getLogin()).get() : null;
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

    @ExceptionHandler(value = { UserByLoginExistsException.class, UserNotFoundException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
    }
}
