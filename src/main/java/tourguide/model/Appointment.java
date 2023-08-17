package tourguide.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name="tourId", referencedColumnName = "id")
    private Tour tour;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<Attendance> attendances;

    @Column(nullable = false)
    private String address;

    private String content;

    private LocalDateTime time;
}