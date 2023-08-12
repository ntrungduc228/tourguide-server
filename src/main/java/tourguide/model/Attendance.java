package tourguide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="destinationId", referencedColumnName = "id",insertable=false, updatable=false)
    private Destination destination;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="destinationId", referencedColumnName = "id",insertable=false, updatable=false)
    private Appointment appointment;

    private Boolean isAttend;

    private AttendanceType type;
}