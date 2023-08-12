package tourguide.payload;

import lombok.*;
import tourguide.model.AttendanceType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListAttendanceDTO {
    private List<Long> userIds;
    private Long destinationId;
    private AttendanceType type;
}