package tourguide.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification extends TimeStamps{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="creatorId", referencedColumnName = "id")
    private User creator;

    @OneToOne
    @JoinColumn(name="receiverId", referencedColumnName = "id")
    private User receiver;

    private Boolean isRead;
}