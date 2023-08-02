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
public class DestinationDTO {
    private Long id;
    private String name;
    private String address;
    private String content;
    private LocalDateTime departureTime;

}