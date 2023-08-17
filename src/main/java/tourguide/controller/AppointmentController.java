package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Appointment;
import tourguide.model.Attendance;
import tourguide.payload.AppointmentDTO;
import tourguide.payload.PostDTO;
import tourguide.payload.ResponseDTO;
import tourguide.service.AppointmentService;
import tourguide.service.NotificationService;
import tourguide.utils.JwtUtil;

@RestController
@RequestMapping("api/appointments")
public class AppointmentController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO, HttpServletRequest request){
        System.out.println("vooo con");
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        Appointment appointmentDTO1 = appointmentService.createAppointment(appointmentDTO, userId);
        if(appointmentDTO1.getAttendances() != null){
            for(Attendance attendance : appointmentDTO1.getAttendances()){
                simpMessagingTemplate.convertAndSend("/topic/appointment/" + attendance.getUser().getId() + "/new", appointmentDTO1);

            }
        }

        return new ResponseEntity<>(new ResponseDTO((appointmentDTO1)), HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updateAppointment(@RequestBody AppointmentDTO appointmentDTO, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        return new ResponseEntity<>(new ResponseDTO((appointmentService.updateAppointment(appointmentDTO))), HttpStatus.OK);
    }

    @GetMapping("{id}/members")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getMembers(@PathVariable Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        return new ResponseEntity<>(new ResponseDTO((appointmentService.getMember(id, userId))), HttpStatus.OK);

    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getAppointmentByTour(@PathVariable Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));

        return new ResponseEntity<>(new ResponseDTO((appointmentService.getListAppointmentByTourId(id))), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));

        return new ResponseEntity<>(new ResponseDTO((appointmentService.deleteAppointment(id))), HttpStatus.OK);
    }

    @PatchMapping("attend")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updateAttendance(@RequestBody AppointmentDTO appointmentDTO, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));

        return new ResponseEntity<>(new ResponseDTO((appointmentService.updateAttendance(appointmentDTO))), HttpStatus.OK);
    }
}