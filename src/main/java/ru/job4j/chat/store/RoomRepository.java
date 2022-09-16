package ru.job4j.chat.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


}
