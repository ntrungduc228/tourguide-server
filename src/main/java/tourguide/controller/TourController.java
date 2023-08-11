package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Tour;
import tourguide.model.User;
import tourguide.payload.MemberDTO;
import tourguide.payload.ResponseDTO;
import tourguide.payload.TourDTO;
import tourguide.service.TourService;
import tourguide.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("api/tours")
public class TourController {

    @Autowired
    JwtUtil jwtUtil;

   @Autowired
    TourService tourService;

   @GetMapping("own")
   @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
   public ResponseEntity<?> getListToursByUserId(HttpServletRequest request){
       Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
       return new ResponseEntity<>(tourService.getListTourByUserId(userId), HttpStatus.OK);
   }

   @GetMapping("{id}")
   @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getTourInfo(@PathVariable Long id, HttpServletRequest request){
       Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
       Tour tour = tourService.getTourById(id, userId);
       return new ResponseEntity<>(tour, HttpStatus.OK);

   }

   @PostMapping("")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createTour(@RequestBody TourDTO tourDTO, HttpServletRequest request){
       Long touristGuideId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
       Tour tour = tourService.createTour(tourDTO, touristGuideId);
       return new ResponseEntity<>(tour, HttpStatus.CREATED);
   }

   @PatchMapping("{id}")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updateTour(@PathVariable Long id, @RequestBody TourDTO tourDTO){
        Tour tour = tourService.updateTour(id, tourDTO);
        return new ResponseEntity<>(tour, HttpStatus.OK);
   }

   @DeleteMapping("{id}")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> deleteTour(@PathVariable Long id){
       return new ResponseEntity<>(tourService.deleteTour(id), HttpStatus.OK);
   }

   @PostMapping("{id}/members/add")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> addMembers(@PathVariable Long id, @RequestBody MemberDTO memberDTO){
       Tour tour = tourService.addMembers(id, memberDTO);
       return new ResponseEntity<>(tour, HttpStatus.OK);
   }

    @PatchMapping("{id}/members/remove")
    @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> removeMembers(@PathVariable Long id, @RequestBody MemberDTO memberDTO){
        Tour tour = tourService.removeMembers(id, memberDTO);
        return new ResponseEntity<>(tour, HttpStatus.OK);
    }

    @PostMapping("{id}/members/join")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> joinRoom(@PathVariable Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        Tour tour = tourService.joinRoom(id, userId);
        return new ResponseEntity<>(tour, HttpStatus.OK);
    }

    @PatchMapping("{id}/members/approve")
    @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> approveMembers(@PathVariable Long id, @RequestBody MemberDTO memberDTO){
        Tour tour = tourService.approveMember(id, memberDTO);
        return new ResponseEntity<>(tour, HttpStatus.OK);
    }

    @DeleteMapping("{id}/members/out")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> outRoom(@PathVariable Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        Tour tour = tourService.outRoom(id, userId);
        return new ResponseEntity<>(tour, HttpStatus.OK);
    }

    @GetMapping("{id}/members")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getMembers(@PathVariable Long id){
        System.out.println(id);
        List<User> users = tourService.getMembers(id);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}