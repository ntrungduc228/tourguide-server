package tourguide.payload;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import tourguide.model.Tour;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDTO {
    private Long id;

    private Tour tour;
    private Long tourId;
    private List<Long> userIds;
    private String address;

    private String content;
    private LocalDateTime time;
}