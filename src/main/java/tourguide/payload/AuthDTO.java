package tourguide.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
}