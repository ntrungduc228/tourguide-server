package tourguide.payload;

import lombok.*;
import tourguide.model.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends TimeStampsDTO{
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Role role;
    private String avatar;

}