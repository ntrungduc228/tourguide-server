package tourguide.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST_GUIDE')")
    public String testUser(){
        return "yo";
    }
}