package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    JwtUtil jwtUtil;

    @PostMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        AppointmentDTO appointmentDTO1 = appointmentService.createAppointment(appointmentDTO, userId);
        return new ResponseEntity<>(new ResponseDTO((appointmentDTO1)), HttpStatus.CREATED);
    }
}