package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tourguide.payload.ResponseDTO;
import tourguide.service.NotificationService;
import tourguide.utils.JwtUtil;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getNotificationsByUserId(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        return new ResponseEntity<>(new ResponseDTO(notificationService.getNotificationsByUserId(userId)), HttpStatus.OK);
    }
}