package tourguide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Post extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String content;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="tourId", referencedColumnName = "id")
    private Tour tour;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "postFile", cascade = CascadeType.ALL)
    private List<File> files;
    private Integer likes;
    Boolean isDelete;
}