package tourguide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String content;
    private LocalDateTime departureTime;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="tourId", referencedColumnName = "id")
    private Tour tour;

    public Destination(Long id, String name, String address, String content, LocalDateTime departureTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.content = content;
        this.departureTime = departureTime;
    }
}