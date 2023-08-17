package tourguide.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.exception.UnAuthorizeException;
import tourguide.model.User;

import tourguide.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userFind = userRepository.findByEmail(email);

        if (userFind.isEmpty()) {
            System.out.println("User not found in database");
//            throw new BadRequestException("Incorrect email or password");
            throw new NotFoundException("Incorrect email or password!!!");

        }
//        return new org.springframework.security.core.userdetails.User(userFind.get().getEmail(),
//                userFind.get().getPassword(), new ArrayList<>());
        return UserDetailsImpl.build(userFind.get());
    }

    public UserDetails loadById(Long id){
        Optional<User> userFind = userRepository.findById(id);

        if (userFind.isEmpty()) {
            System.out.println("User not found in database");
            throw new NotFoundException("Unauthorized!!!");

//            return null;
        }
        return UserDetailsImpl.build(userFind.get());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}