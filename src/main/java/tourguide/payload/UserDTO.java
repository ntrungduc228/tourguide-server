package tourguide.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends TimeStampsDTO{
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;

}