package tourguide.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tourguide.exception.UnAuthorizeException;
import tourguide.security.CustomUserDetailsService;
import tourguide.utils.JwtUtil;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // Simple JWT implementation
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseJwt(request);
        if(token != null && jwtUtil.validateJwtToken(token)){
            Long userId = jwtUtil.getUserId(token);
            UserDetails userDetails = userDetailsService.loadById(userId);

            // initializing UsernamePasswordAuthenticationToken with its 3 parameter constructor
            // because it sets super.setAuthenticated(true); in that constructor.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // finally, give the authentication token to Spring Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // end of the method, so go for next filter class
        }
        filterChain.doFilter(request, response);

    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // trying to find Authorization header
//        final String authorizationHeader = request.getHeader("Authorization");
//        System.out.println("check here jwt");
//        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer")){
//            // if Authorization header does not exist, then skip this filter
//            // and continue to execute next filter class
//            System.out.println("empty header");
////            throw new UnAuthorizeException("Unauthorized");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String token = authorizationHeader.split(" ")[1].trim();
//        if (!jwtUtil.validate(token)) {
//            // if token is not valid, then skip this filter
//            // and continue to execute next filter class.
//            // This means authentication is not successful since token is invalid.
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Authorization header exists, token is valid. So, we can authenticate.
//        String username = jwtUtil.getUsername(token);
//        // initializing UsernamePasswordAuthenticationToken with its 3 parameter constructor
//        // because it sets super.setAuthenticated(true); in that constructor.
//        UsernamePasswordAuthenticationToken upassToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
//        upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        // finally, give the authentication token to Spring Security Context
//        SecurityContextHolder.getContext().setAuthentication(upassToken);
//
//        // end of the method, so go for next filter class
//        filterChain.doFilter(request, response);
//    }
}