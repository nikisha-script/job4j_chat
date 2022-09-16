package ru.job4j.chat.serivce;

import org.springframework.stereotype.Service;
import ru.job4j.chat.entity.Message;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.store.MessageRepository;
import ru.job4j.chat.store.UserRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Message save(Message message, Long id) {
        User user = userRepository.findById(id).get();
        message.setUser(user);
        return messageRepository.save(message);
    }

}
