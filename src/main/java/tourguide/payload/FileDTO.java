package tourguide.payload;

import lombok.*;
import tourguide.model.FileType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO extends TimeStampsDTO{
    private Long id;
    private String link;

    public FileDTO(Long id, String link, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
        this.link = link;
        this.id = id;
        this.setCreatedAt(createdAt);
        this.setLastModifiedDate(lastModifiedDate);
    }
}