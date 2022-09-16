package ru.job4j.chat.model;

import lombok.*;
import ru.job4j.chat.entity.Message;
import ru.job4j.chat.entity.Role;
import ru.job4j.chat.entity.User;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    @EqualsAndHashCode.Include
    @NonNull
    private Long id;
    @NonNull
    private String login;
    @NonNull
    private String email;
    @NonNull
    private List<Role> roles;
    @NonNull
    private List<Message> messages;

    public static UserDto getUserDto(User user) {
        List<Role> roleList = user.getRoles();
        List<Message> messageList = user.getMessages();
        return new UserDto(user.getId(),
                user.getLogin(),
                user.getEmail(),
                roleList,
                messageList);
    }

}
