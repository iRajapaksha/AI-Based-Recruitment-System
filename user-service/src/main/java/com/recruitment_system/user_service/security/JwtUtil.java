package com.recruitment_system.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "sectretkey123werthyjh24356ujhgfewqrgthgnbvdewrgthnwergthyjnbsehrtytsgratthrjyndgbfrathydgfbvd";
    SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());

    private final long EXPIRATION = 1000*60*60;
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
