package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourguide.model.Tour;
import tourguide.model.User;
import tourguide.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public String testUser(){
        return "yo";
    }

    @GetMapping("/search/phone")
    public ResponseEntity<?> findByPhone(@RequestParam("q") String phone) throws Exception {
        List<User> users = userService.findByPhone(phone);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}