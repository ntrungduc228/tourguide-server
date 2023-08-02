package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Tour;
import tourguide.payload.ResponseDTO;
import tourguide.payload.TourDTO;
import tourguide.service.TourService;

@RestController
@RequestMapping("api/tours")
public class TourController {

   @Autowired
    TourService tourService;

   @PostMapping("")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createTour(@RequestBody TourDTO tourDTO){
       Tour tour = tourService.createTour(tourDTO);
       return new ResponseEntity<>(new ResponseDTO(tour), HttpStatus.CREATED);
   }

   @PatchMapping("{id}")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updateTour(@PathVariable Long id, @RequestBody TourDTO tourDTO){
        Tour tour = tourService.updateTour(id, tourDTO);
        return new ResponseEntity<>(new ResponseDTO(tour), HttpStatus.OK);
   }

   @DeleteMapping("{id}")
   @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> deleteTour(@PathVariable Long id){
       return new ResponseEntity<>(new ResponseDTO(tourService.deleteTour(id)), HttpStatus.OK);
   }

}