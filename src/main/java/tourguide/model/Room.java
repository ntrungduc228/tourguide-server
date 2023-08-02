package tourguide.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room extends TimeStamps  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="tourId", referencedColumnName = "id")
    private Tour roomTour;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User roomUser;
}