package tourguide.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.exception.UnAuthorizeException;
import tourguide.security.CustomUserDetailsService;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    // Injecting available encryption bean
    @Autowired
    PasswordEncoder passwordEncoder;

    // Injecting our custom UserDetailsService implementation
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // In BasicController.login() method, we call authenticationManager.authenticate(token)
        // Then, Authentication Manager calls AuthenticationProvider's authenticate method.
        // Since JwtAuthenticationProvider is our custom authentication provider,
        // this method will be executed.
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        System.out.println("jwt provider " + username);

        // Fetching user as wrapped with UserDetails object
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println("user detail " + userDetails.getUsername());

        // If user is not null, then we check if password matches
        if (userDetails != null){
            if (passwordEncoder.matches(password, userDetails.getPassword())){
                // if it matches, then we can initialize UsernamePasswordAuthenticationToken.
                // Attention! We used its 3 parameters constructor.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());
                return authenticationToken;
            }
        }
//        throw new UnAuthorizeException("efsdfsd123123");
//        throw new BadCredentialsException("Incorrect email or password!!");
        throw new NotFoundException("Incorrect email or password!!!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}