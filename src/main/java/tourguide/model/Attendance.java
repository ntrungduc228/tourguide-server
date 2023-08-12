package tourguide.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Attendance extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name="destinationId", referencedColumnName = "id")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name="appointmentId", referencedColumnName = "id")
    private Appoinment appoinment;

    private Boolean isAttend;
}