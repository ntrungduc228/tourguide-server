package tourguide.payload;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private UserDTO user;
    private Long postId;
    private Long parentId;
    private Boolean isDelete;
}