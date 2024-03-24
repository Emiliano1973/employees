package com.employees.demo.security.impl;

import com.employees.demo.security.JwtUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtilsImpl implements JwtUtils {

    private final String jwtSecret;
    private final int jwtExpirationMs;

    public JwtUtilsImpl( @Value("${employees.app.jwtSecret}") final String jwtSecret,
                         @Value("${employees.app.jwtExpirationMs}") final int jwtExpirationMs) {
        super();
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    public String generateJwtToken(final String username,final Collection<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claims(Map.<String, String[]>of("roles", roles.toArray(new String[roles.size()])))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateJwtToken(final String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getUserNameFromJwtToken(final String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    @Override
    public boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return !isExpired(authToken);
        } catch (MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT token: {"+e.getMessage()+"}");
        }
        return false;
    }

    private boolean isExpired(final String authToken){
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(authToken).getBody()
                .getExpiration().before(new Date());
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
