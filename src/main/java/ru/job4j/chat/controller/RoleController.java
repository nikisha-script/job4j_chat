package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.entity.Role;
import ru.job4j.chat.exception.RoleByNameExistsException;
import ru.job4j.chat.serivce.RoleService;

import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Role> save(@RequestBody Role role, @RequestParam() Long userId) {
        Optional<Role> temp = service.findAllByName(role.getName());
        if (temp.isPresent()) {
            throw new RoleByNameExistsException("role with this name already exists");
        }
        return new ResponseEntity<>(
                service.save(role, userId),
                HttpStatus.CREATED
        );
    }

}
