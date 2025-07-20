package com.ai_based_recruitment_system.security;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JWTGenerator {

    SecretKey key = new SecretKeySpec(SecurityConstants.JWT_SECRET.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
        return token;
    }

    public String getUsernameFromJWT(String token) {

        // Parse the JWT and extract claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Use the updated method with a Key object
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    public String getRolesFromJWT(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", String.class); // Extract roles claim
    }


    public boolean validateToken(String token) {
        try {

            // Use JwtParserBuilder for parsing and validation
            Jwts.parserBuilder()
                    .setSigningKey(key) // Use the updated method with a Key object
                    .build()
                    .parseClaimsJws(token); // Parse the token

            return true;
        } catch (Exception ex) {
            // Handle invalid or expired JWTs
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", ex);
        }
    }
}
