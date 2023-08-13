package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.NotFoundException;
import tourguide.model.*;
import tourguide.payload.AppointmentDTO;
import tourguide.payload.AttendanceDTO;
import tourguide.repository.AppointmentRepository;
import tourguide.repository.AttendanceRepository;

import java.util.ArrayList;
import java.util.List;
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

    public Appointment createAppointment(AppointmentDTO appointmentDTO, Long userId){
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
        return appointment;
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
        System.out.println("ssfdsf " + appointment.getId());

        Attendance attendance = new Attendance();
        attendance.setIsAttend(false);
        attendance.setUser(user);
        attendance.setAppointment(appointment);
        Attendance newAttendance = attendanceRepository.save(attendance);
        return  buildAttendanceDTO(attendance);
    }

    // get diem hen
    public List<AppointmentDTO> getListAppointmentByTourId(Long tourId){
        Tour tour = tourService.findById(tourId);
        List<Appointment> appointments = appointmentRepository.findByTour(tour);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for(Appointment appointment :appointments){
            appointmentDTOS.add(buildAppointmentDTO(appointment));
        }
        return appointmentDTOS;
    }
    // sua diem hen
    public Appointment updateAppointment(AppointmentDTO appointmentDTO){
        Appointment appointment = findById(appointmentDTO.getId());
        appointment.setAddress(appointmentDTO.getAddress());
        appointment.setContent(appointmentDTO.getContent());
        appointment.setTime(appointmentDTO.getTime());
        return appointmentRepository.save(appointment);
    }

    // xoa diem hen
    public Appointment deleteAppointment(Long id){
        Appointment appointment = findById(id);
        appointmentRepository.delete(appointment);
        return appointment;
    }

    // diem danh
    public Appointment updateAttendance(AppointmentDTO appointmentDTO){
        Appointment appointment = findById(appointmentDTO.getId());
        if(appointmentDTO.getUserIds()!= null && appointmentDTO.getUserIds().size() > 0){
            List<Attendance> attendances = appointment.getAttendances();
            for(Long userId: appointmentDTO.getUserIds()){
                for(Attendance  attendance : attendances){
                    if(attendance.getUser().getId() == userId){
                        attendance.setIsAttend(true);
                    }
                }
            }
            appointment.setAttendances(attendances);
        }
        Appointment newAppointment = appointmentRepository.save(appointment);

        return appointment;
    }

}