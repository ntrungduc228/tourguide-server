package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Notification;
import tourguide.model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(User receiver);
    List<Notification> findByReceiverAndIsRead(User receiver, Boolean isRead);
}
