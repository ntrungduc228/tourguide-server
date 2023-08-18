package tourguide.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tourguide.model.Destination;
import tourguide.model.NotiData;
import tourguide.model.NotificationType;
import tourguide.model.Room;
import tourguide.service.DestinationService;
import tourguide.service.NotificationService;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class Scheduler {

    @Autowired
    DestinationService destinationService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    NotificationService notificationService;

    @Scheduled(fixedDelay = 1000 * 15, initialDelay = 3000)
    public void fixedDelaySch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Fixed Delay scheduler:: " + strDate);

        checkScheduleDestination();
        checkScheduleAppointment();
    }

    void checkScheduleDestination() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Destination> destinationList = destinationService.findScheduleDestination();
        for(Destination destination :destinationList){
            String message = destination.getName() + "sẽ diễn ra lúc " + formatter.format(destination.getDepartureTime());
            for(Room room : destination.getTour().getRooms()){
                NotiData notiData = new NotiData().builder()
                        .data(message)
                        .type(NotificationType.DESTINATION_COMING)
                        .build();
                simpMessagingTemplate.convertAndSend("/topic/noti/" + room.getRoomUser().getId() + "/new", notiData);

            }
        }
    }

    void checkScheduleAppointment(){

    }
}