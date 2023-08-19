package tourguide.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tourguide.model.*;
import tourguide.service.AppointmentService;
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
    AppointmentService appointmentService;

    @Autowired
    NotificationService notificationService;

    @Scheduled(fixedDelay = 1000 * 15*60, initialDelay = 3000)
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<Appointment> appointments = appointmentService.findScheduleAppointment();
        for(Appointment appointment : appointments){
            String message = "Điểm hẹn " +appointment.getAddress() + "sẽ diễn ra lúc " + formatter.format(appointment.getTime());
            for(Attendance attendance : appointment.getAttendances()){
                NotiData notiData = new NotiData().builder()
                        .data(message)
                        .type(NotificationType.APPOINTMENT_COMING)
                        .build();
                simpMessagingTemplate.convertAndSend("/topic/noti/" + attendance.getUser().getId() + "/new", notiData);
            }
        }
    }
}