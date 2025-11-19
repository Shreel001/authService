package com.bankingApp.authService.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private SecretKey getSiginingKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSiginingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date extractExpiry(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities){
        Map<String, Object> claims = new HashMap<>();

        Set<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return createToken(claims, username, roles);
    }


    private String createToken(Map<String, Object> claims, String subject, Set<String> authorities){
        claims.put("authorities", authorities);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 minutes expiration time
                .signWith(getSiginingKey())
                .compact();
    }

    private boolean isTokenExpired(String token){return extractExpiry(token).before(new Date());}

    public boolean validateToken(String token){return !isTokenExpired(token);}

}