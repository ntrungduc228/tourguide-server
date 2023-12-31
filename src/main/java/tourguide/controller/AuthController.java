package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tourguide.model.User;
import tourguide.payload.*;
import tourguide.service.UserService;
import tourguide.utils.JwtUtil;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("signin")
    public ResponseEntity<?> signIn(@RequestBody LoginDTO loginDTO){
        System.out.println(" lag tung chao");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = jwtUtil.generateToken(authentication);
        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);

    }



    @PostMapping("signup")
    public ResponseEntity<?> signUp(@RequestBody AuthDTO authDTO){
        UserDTO userDTO = userService.signUp(authDTO);
        return new ResponseEntity<>(new ResponseDTO(userDTO), HttpStatus.CREATED);
    }


    @GetMapping("info")
    public ResponseEntity<?> getInfo(HttpServletRequest request){
        System.out.println("Get info user");
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        return new ResponseEntity<>(new ResponseDTO(userService.getInfo(userId)), HttpStatus.OK);
    }

    @GetMapping("{email}")
    public ResponseEntity<?> findUser(@PathVariable String email) throws Exception {
        User user = userService.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}