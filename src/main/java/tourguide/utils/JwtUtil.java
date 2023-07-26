package tourguide.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tourguide.security.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {
    private static final int expireInMs = 60 * 1000 * 1000 * 24;

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private String secret = "secret";

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("key " + userDetails.getId() + userDetails.getEmail());
        return Jwts.builder()
                .setSubject(String.valueOf(userDetails.getId()))
                .setIssuer("tourguide")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String generate(String data) {
        System.out.println("key " + key);
        return Jwts.builder()
                .setSubject(data)
                .setIssuer("tourguide")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
    public boolean validate(String token) {
        if (getUsername(token) != null && isExpired(token)) {
            return true;
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}