package ru.job4j.chat.serivce;

import org.springframework.stereotype.Service;
import ru.job4j.chat.entity.Room;
import ru.job4j.chat.entity.User;
import ru.job4j.chat.store.RoomRepository;
import ru.job4j.chat.store.UserRepository;

import java.util.List;

@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public RoomService(UserRepository userRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public Room addUserAtRoom(Long roomId, Long userId) {
        User user = userRepository.findById(userId).get();
        Room room = roomRepository.findById(roomId).get();
        room.adduser(user);
        return roomRepository.save(room);
    }
}
