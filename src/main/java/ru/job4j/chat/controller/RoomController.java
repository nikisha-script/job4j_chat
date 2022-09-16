package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.entity.Room;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.serivce.RoomService;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/add")
    public ResponseEntity<Room> addUserToRoom(@RequestParam Long roomId, @RequestParam Long userId) {
        return new ResponseEntity<>(
                roomService.addUserAtRoom(roomId, userId),
                HttpStatus.CREATED
        );
    }

    @PostMapping()
    public ResponseEntity<Room> save(@RequestBody Room room) {
        return new ResponseEntity<>(
                roomService.save(room),
                HttpStatus.CREATED
        );
    }
}
