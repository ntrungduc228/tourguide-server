package tourguide.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDto {
    private String content;
    private String username;
}