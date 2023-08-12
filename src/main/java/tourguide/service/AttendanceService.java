package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.model.*;
import tourguide.payload.AttendanceDTO;
import tourguide.repository.AttendanceRepository;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserService userService;

    @Autowired
    DestinationService destinationService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    RoomService roomService;

    public Attendance createAttendance(AttendanceDTO attendanceDTO){
        Destination destination = null;
        Appointment appointment = null;
        User user = userService.findById(attendanceDTO.getUserId());
        if(attendanceDTO.getType().equals(AttendanceType.DESTINATION)){
            destination = destinationService.findById(attendanceDTO.getDestinationId());
            roomService.findByRoomUserAndRoomTour(user, destination.getTour());
        }else {
            appointment = appointmentService.findById(attendanceDTO.getDestinationId());
            roomService.findByRoomUserAndRoomTour(user, appointment.getDestination().getTour());
        }

        Attendance attendance = new Attendance();
        attendance.setIsAttend(true);
        attendance.setUser(user);
        if(attendanceDTO.getType().equals(AttendanceType.DESTINATION)){
            attendance.setDestination(destination);
        }else {
            attendance.setAppointment(appointment);
        }
        return attendanceRepository.save(attendance);
    }
}