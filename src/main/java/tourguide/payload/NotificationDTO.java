package tourguide.payload;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
import tourguide.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO extends TimeStampsDTO{

    private Long id;

       private Long creatorId;
       private Long receiverId;
    private UserDTO creator;


    private UserDTO receiver;

    private Boolean isRead;

    private String content;
}