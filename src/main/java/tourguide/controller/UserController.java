package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Tour;
import tourguide.model.User;
import tourguide.payload.CommentDTO;
import tourguide.payload.ResponseDTO;
import tourguide.payload.UserDTO;
import tourguide.service.UserService;
import tourguide.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public String testUser(){
        return "yo";
    }



    @GetMapping("/search/phone")
    public ResponseEntity<?> findByPhone(@RequestParam("q") String phone, HttpServletRequest request) throws Exception {
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        List<User> users = userService.findByPhone(phone, userId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PatchMapping("")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO newUser){
     UserDTO user = userService.updateProfile(newUser);

        return new ResponseEntity<>(new ResponseDTO((user)), HttpStatus.OK);
    }
}