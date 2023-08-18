package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("own")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getNotificationsByUserId(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        simpMessagingTemplate.convertAndSend("/topic/messagess123", "test cai");
        return new ResponseEntity<>(new ResponseDTO(notificationService.getNotificationsByUserId(userId)), HttpStatus.OK);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> readAllNotification(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        simpMessagingTemplate.convertAndSend("/topic/noti" + userId + "/update", userId);
        return new ResponseEntity<>(new ResponseDTO(notificationService.readAllNotification(userId)), HttpStatus.OK);
    }

}