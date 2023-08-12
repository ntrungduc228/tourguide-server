package tourguide.payload;

import lombok.*;
import tourguide.model.AttendanceType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {
    private Long id;
    private Long userId;
    private Long destinationId;
    private Boolean isAttend;
    private AttendanceType type;
    private UserDTO user;
}