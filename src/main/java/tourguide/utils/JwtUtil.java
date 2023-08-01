package tourguide.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tourguide.security.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final int expireInMs = 60 * 1000 * 1000 * 24;
//    private static final int expireInMs =  1000*60*5;

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private String getSignInKey() {
//        return Encoders.BASE64.encode(key.getEncoded());
        return "gnS6HR90tK7IZxsLzXkQRMtND54jGSfJh6nCjPo7iLs=";
    }

    public String generateToken(Authentication authentication) {
        System.out.println("key " +getSignInKey());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(String.valueOf(userDetails.getId()))
                .setIssuer("tourguide")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
//                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generate(String data) {
        System.out.println("key " + key);
        return Jwts.builder()
                .setSubject(data)
                .setIssuer("tourguide")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
//                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public boolean validate(String token) {
        if (getUserId(token) != null && isExpired(token)) {
            return true;
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        String id = claims.getSubject();
        return Long.valueOf(id);
    }

//    public String getUsername(String token) {
//        Claims claims = getClaims(token);
//        return claims.getSubject();
//    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody();
    }
}