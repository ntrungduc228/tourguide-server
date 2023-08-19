package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.NotFoundException;
import tourguide.model.*;
import tourguide.payload.AppointmentDTO;
import tourguide.payload.AttendanceDTO;
import tourguide.repository.AppointmentRepository;
import tourguide.repository.AttendanceRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                .id(appointment.getId())
                .address(appointment.getAddress())
                .content(appointment.getContent())
                .time(appointment.getTime())
                .tourId(appointment.getTour().getId())
                .tour(appointment.getTour())
                .userId(appointment.getCreator().getId())
                .user(userService.buildUserDTO(appointment.getCreator()))
                .build();
        return appointmentDTO;
    }

    public List<Attendance> getMember(Long id, Long userId){
        Appointment appointment = findById(id);
        return appointment.getAttendances();
    }

    public Appointment createAppointment(AppointmentDTO appointmentDTO, Long userId){
        User user = userService.findById(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println(String.format("formatter " + formatter.format(appointmentDTO.getTime())));
        Appointment appointment = new Appointment().builder()
                .address(appointmentDTO.getAddress())
                .content(appointmentDTO.getContent())
                .time(appointmentDTO.getTime())
                .creator(user)
                .tour(tourService.findById(appointmentDTO.getTourId()))
                .build();
        List<Attendance> attendances = new ArrayList<>();
        System.out.println("a " + appointmentDTO.getUserIds().size());
        if(appointmentDTO.getUserIds()!= null && appointmentDTO.getUserIds().size() > 0){
            for(Long receiverId : appointmentDTO.getUserIds()){
                Attendance attendance = new Attendance();
                attendance.setIsAttend(false);
                attendance.setUser(userService.findById(receiverId));
                attendance.setAppointment(appointment);
                attendances.add(attendance);
//                AttendanceDTO attendanceDTO = new AttendanceDTO();
//                attendanceDTO.setUserId(receiverId);
////                attendanceDTO.setAppointmentId(appointment.getId());
//                attendances.add(createAttendance(attendanceDTO)) ;
            }
        }
        appointment.setAttendances(attendances);

        Appointment newAppointment = appointmentRepository.save(appointment);
        for(Attendance attendance : newAppointment.getAttendances()){
            System.out.println("hehehe");
            notificationService.notify(attendance.getUser().getId(), userId, NotificationType.NEW_APPOINTMENT);

        }
        return newAppointment;
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

    public Attendance createAttendance(AttendanceDTO attendanceDTO){
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
//        return  buildAttendanceDTO(attendance);
        return newAttendance;
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

    public List<Appointment> findScheduleAppointment(){
        List<Appointment> appointments = appointmentRepository.findByTimeGreaterThan(LocalDateTime.now());
        List<Appointment> appointmentList = new ArrayList<>();
        for(Appointment appointment: appointments){
            Duration duration = Duration.between(appointment.getTime(), LocalDateTime.now());
            if(duration.toMinutes() >= -5){
                appointmentList.add(appointment);

            }

//            System.out.println(" duration +" + duration.toMinutes() + " "+ formatter.format(destination.getDepartureTime()) );
        }
        return appointmentList;
    }

}