package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.NotFoundException;
import tourguide.model.*;
import tourguide.payload.AppointmentDTO;
import tourguide.payload.AttendanceDTO;
import tourguide.repository.AppointmentRepository;
import tourguide.repository.AttendanceRepository;

import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    TourService tourService;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    public Appointment findById(Long id){
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if(optionalAppointment.isEmpty()){
            throw new NotFoundException("Không tìm thấy điểm hẹn");
        }
        return optionalAppointment.get();
    }

    public AppointmentDTO buildAppointmentDTO(Appointment appointment){
        AppointmentDTO appointmentDTO = new AppointmentDTO().builder()
                .address(appointment.getAddress())
                .content(appointment.getContent())
                .time(appointment.getTime())
                .tourId(appointment.getTour().getId())
                .build();
        return appointmentDTO;
    }

    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO, Long userId){
        Appointment appointment = new Appointment().builder()
                .address(appointmentDTO.getAddress())
                .content(appointmentDTO.getContent())
                .time(appointmentDTO.getTime())
                .tour(tourService.findById(appointmentDTO.getTourId()))
                .build();
        Appointment newAppointment = appointmentRepository.save(appointment);
        if(appointmentDTO.getUserIds()!= null && appointmentDTO.getUserIds().size() > 0){
            for(Long receiverId : appointmentDTO.getUserIds()){
                AttendanceDTO attendanceDTO = new AttendanceDTO();
                attendanceDTO.setUserId(receiverId);
                attendanceDTO.setAppointmentId(newAppointment.getId());
                createAttendance(attendanceDTO);
                notificationService.notify(receiverId, userId, NotificationType.NEW_APPOINTMENT);
            }
        }
        return buildAppointmentDTO(appointment);
    }

    public AttendanceDTO buildAttendanceDTO(Attendance attendance){
        AttendanceDTO attendanceDTO = new AttendanceDTO().builder()
                .id(attendance.getId())
                .isAttend(attendance.getIsAttend())
                .appointmentId(attendance.getAppointment().getId())
                .user(userService.buildUserDTO(attendance.getUser())
                )
                .build();
        return attendanceDTO;
    }

    public AttendanceDTO createAttendance(AttendanceDTO attendanceDTO){
        Destination destination = null;
        Appointment appointment = null;
        User user = userService.findById(attendanceDTO.getUserId());
        appointment = findById(attendanceDTO.getAppointmentId());
//        roomService.findByRoomUserAndRoomTour(user, appointment.getDestination().getTour());

        Attendance attendance = new Attendance();
        attendance.setIsAttend(false);
        attendance.setUser(user);
        attendance.setAppointment(appointment);
        Attendance newAttendance = attendanceRepository.save(attendance);
        return  buildAttendanceDTO(attendance);
    }
}