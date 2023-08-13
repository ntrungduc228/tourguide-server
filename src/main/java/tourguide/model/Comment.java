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
public class Comment extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="postId", referencedColumnName = "id")
    private Post post;

    private Boolean isDelete;
    private Long parentId;

}