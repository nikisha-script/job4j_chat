package ru.job4j.chat.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
