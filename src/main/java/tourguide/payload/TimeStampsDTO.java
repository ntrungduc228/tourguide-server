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
public class TimeStampsDTO {
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedDate;
}