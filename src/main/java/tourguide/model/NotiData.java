package tourguide.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import tourguide.payload.NotificationDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotiData {
    private Notification notificationData;
    private NotificationDTO notification;
    private Object data;
    private NotificationType type;
}