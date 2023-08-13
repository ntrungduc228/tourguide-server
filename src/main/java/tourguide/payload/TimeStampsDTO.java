package tourguide.payload;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeStampsDTO {
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedDate;
}