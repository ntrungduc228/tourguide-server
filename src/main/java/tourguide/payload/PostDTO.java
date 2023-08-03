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
    private Boolean isDelete;
    private int likes;

    public PostDTO(Long id, String content, List<FileDTO> filesReturn, Boolean isDelete, Long tourId, Long userId, Integer likes, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.content = content;
        this.files = filesReturn;
        this.tourId = tourId;
        this.userId = userId;
        this.likes = likes;
        this.isDelete = isDelete;
        this.setCreatedAt(createdAt);
        this.setLastModifiedDate(lastModifiedDate);
    }
}