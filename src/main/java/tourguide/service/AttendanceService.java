package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.model.*;
import tourguide.payload.AttendanceDTO;
import tourguide.payload.ListAttendanceDTO;
import tourguide.payload.UserDTO;
import tourguide.repository.AttendanceRepository;

import java.util.List;
import java.util.Optional;

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
        appointment = appointmentService.findById(attendanceDTO.getAppointmentId());
//        roomService.findByRoomUserAndRoomTour(user, appointment.getDestination().getTour());

        Attendance attendance = new Attendance();
        attendance.setIsAttend(true);
        attendance.setUser(user);
        attendance.setAppointment(appointment);
        Attendance newAttendance = attendanceRepository.save(attendance);
        return  buildAttendanceDTO(attendance);
    }

    public AttendanceDTO updateAttendance(Long id, AttendanceDTO attendanceDTO){
        Optional<Attendance> attendanceOptional = attendanceRepository.findById(id);
        if(attendanceOptional.isEmpty()){
            return createAttendance(attendanceDTO);
        }
        attendanceOptional.get().setIsAttend(!attendanceOptional.get().getIsAttend());
        Attendance newAttendance = attendanceRepository.save(attendanceOptional.get());
        return buildAttendanceDTO(newAttendance);
    }

//    public List<AttendanceDTO> attendDestination(ListAttendanceDTO listAttendanceDTO){
//        if(listAttendanceDTO == null || listAttendanceDTO.getUserIds().size() == 0){
//            return  null;
//        }
//
//
//    }

}