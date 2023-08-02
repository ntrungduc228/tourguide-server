package tourguide.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tour extends TimeStamps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Destination> destinations;

    @OneToMany(mappedBy = "roomTour", cascade = CascadeType.ALL)
    private List<Room> rooms;
}