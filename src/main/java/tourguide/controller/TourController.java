package tourguide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tourguide.payload.ResponseDTO;

@RestController
@RequestMapping("api/tours")
public class TourController {

    @GetMapping()
    public String getTours(){
//        return new ResponseEntity<>(new ResponseDTO("hihih"), HttpStatus.OK);
        return "hihh";
    }
}