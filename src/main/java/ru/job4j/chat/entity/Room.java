package ru.job4j.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Id must be non null")
    private Long id;

    @NotBlank(message = "name must be not empty")
    @Min(value = 3, message = "name must be more than 3")
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
