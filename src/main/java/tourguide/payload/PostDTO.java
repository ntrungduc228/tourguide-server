package tourguide.payload;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO extends TimeStampsDTO{
    private Long id;
    private String content;
    private List<FileDTO> files;
    private Long tourId;
    private Long userId;
    private UserDTO user;
    private Boolean isDelete;
    private int likes;
    private Integer comments;

    public PostDTO(Long id, String content, List<FileDTO> filesReturn, Boolean isDelete, Long tourId, Long userId, UserDTO user,
                   Integer likes,
                   Integer comments,
                   LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.content = content;
        this.files = filesReturn;
        this.tourId = tourId;
        this.userId = userId;
        this.likes = likes;
        this.user=  user;
        this.comments = comments;
        this.isDelete = isDelete;
        this.setCreatedAt(createdAt);
        this.setLastModifiedDate(lastModifiedDate);
    }
}