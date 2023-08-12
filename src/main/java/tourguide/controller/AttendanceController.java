package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tourguide.payload.AttendanceDTO;
import tourguide.payload.ResponseDTO;
import tourguide.service.AttendanceService;

@RestController
@RequestMapping("api/attendance")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @PostMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createAttendance(@RequestBody AttendanceDTO attendanceDTO){
        return new ResponseEntity<>(new ResponseDTO(attendanceService.createAttendance(attendanceDTO)), HttpStatus.CREATED);
    }


}