package com.employees.demo.security.impl;

import com.employees.demo.security.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class JwtUtilsImpl implements JwtUtils {
    private final String jwtSecret;
    private final int jwtExpirationMs;

    public JwtUtilsImpl(final String jwtSecret, final int jwtExpirationMs) {
        super();
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    public String generateJwtToken(final String username,final String[] roles) {
        return Jwts.builder()
                .subject(username)
                .claims(Map.<String, String[]>of("roles", roles))
                .issuedAt(new Date())
                .expiration(getExpiresDate())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateJwtToken(final String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(getExpiresDate())
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
            return true;
        } catch (MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT token: {"+e.getMessage()+"}");
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
    }

    private Date getExpiresDate(){
        return Date.from(LocalDateTime
                .now().plusMinutes(this.jwtExpirationMs)
                .atZone(ZoneId.systemDefault()).toInstant());
    }
}
