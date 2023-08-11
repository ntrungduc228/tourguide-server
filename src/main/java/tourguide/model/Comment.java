package tourguide.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name="tourId", referencedColumnName = "id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name="postId", referencedColumnName = "id")
    private Post post;

    private Boolean isDelete;
    private Long parentId;

}