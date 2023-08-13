package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.model.Notification;
import tourguide.model.User;
import tourguide.payload.NotificationDTO;
import tourguide.payload.UserDTO;
import tourguide.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserService userService;

    public NotificationDTO buildNotificationDTO(Notification notification){
        if(notification == null) return null;
        NotificationDTO notificationDTO = new NotificationDTO().builder()
                .id(notification.getId())
                .content(notification.getContent())
                .creator(userService.buildUserDTO(notification.getCreator()))
                .receiver(userService.buildUserDTO(notification.getReceiver()))
                .isRead(notification.getIsRead())
                .build();
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        notificationDTO.setLastModifiedDate(notification.getLastModifiedDate());
        return notificationDTO;
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        User creator = userService.findById(notificationDTO.getCreatorId());
        User receiver = userService.findById(notificationDTO.getReceiverId());
        Notification notification = new Notification();
        notification.setCreator(creator);
        notification.setReceiver(receiver);
        notification.setIsRead(false);
        notification.setContent(notificationDTO.getContent());
        Notification newNotification = notificationRepository.save(notification);
        return buildNotificationDTO(newNotification);
    }

    public List<NotificationDTO> getNotificationsByUserId (Long userId){
        User receiver = userService.findById(userId);
        List<Notification> notifications = notificationRepository.findByReceiver(receiver);
        if(notifications != null){
            List<NotificationDTO> notificationDTOS = new ArrayList<>();
            for(Notification notification : notifications){
                notificationDTOS.add(buildNotificationDTO(notification));
            }
            return notificationDTOS;
        }

        return null;
    }
}