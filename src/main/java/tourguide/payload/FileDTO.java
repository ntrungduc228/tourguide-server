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
    private FileType fileType;

    public FileDTO(Long id, String link, FileType type, LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.fileType = type;
        this.setCreatedAt(createdAt);
        this.setLastModifiedDate(lastModifiedDate);
    }
}