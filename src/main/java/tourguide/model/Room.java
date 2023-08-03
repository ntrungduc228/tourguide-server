package tourguide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"tourId", "userId"})})
public class Room extends TimeStamps  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="tourId", referencedColumnName = "id")
    private Tour roomTour;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User roomUser;

    private Boolean isApproved;
}