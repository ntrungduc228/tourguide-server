package tourguide.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Appointment extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String address;

    @ManyToOne
    @JoinColumn(name="destinationId", referencedColumnName = "id")
    private Destination destination;

    private String content;
    private LocalDateTime time;
}