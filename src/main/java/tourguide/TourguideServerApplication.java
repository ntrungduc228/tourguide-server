package tourguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@EnableScheduling
public class TourguideServerApplication {



	public static void main(String[] args) {
		SpringApplication.run(TourguideServerApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@GetMapping("/")
	public ResponseEntity<?> getHome(){
		return new ResponseEntity<>("TourGuide Server", HttpStatus.OK);
	}
}
