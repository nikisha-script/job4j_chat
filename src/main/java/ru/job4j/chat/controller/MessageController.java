package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.entity.Message;
import ru.job4j.chat.serivce.MessageService;

@RestController
@RequestMapping("/msg")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Message> save(@RequestBody Message message, @RequestParam() Long userId) {
        return new ResponseEntity<>(
                service.save(message, userId),
                HttpStatus.CREATED
        );
    }
}
