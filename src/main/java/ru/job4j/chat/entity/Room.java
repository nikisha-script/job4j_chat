package ru.job4j.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_rooms",
            joinColumns = {@JoinColumn(name = "id_room")},
            inverseJoinColumns = {@JoinColumn(name = "id_user")})
    @JsonIgnore
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Message> messages;

    public void adduser(User user) {
        users.add(user);
    }

}
