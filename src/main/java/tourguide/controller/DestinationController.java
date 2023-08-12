package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.payload.DestinationDTO;
import tourguide.payload.ResponseDTO;
import tourguide.service.DestinationService;
import tourguide.utils.JwtUtil;

@RestController
@RequestMapping("api/destinations")
public class DestinationController {
    @Autowired
    DestinationService destinationService;

    @Autowired
    JwtUtil jwtUtil;

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody DestinationDTO destinationDTO, HttpServletRequest request){
        return new ResponseEntity<>(new ResponseDTO((destinationService.updateDestination(destinationDTO, id))), HttpStatus.OK);
    }
}