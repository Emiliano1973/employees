package com.employees.demo.security.impl;

import com.employees.demo.security.JwtUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class JwtUtilsImpl implements JwtUtils {
    private static final Log logger = LogFactory.getLog(JwtUtilsImpl.class);

    private final String jwtSecret;
    private final int jwtExpirationMin;

    public JwtUtilsImpl(final String jwtSecret, final int jwtExpirationMin) {
        super();
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMin = jwtExpirationMin;
    }

    @Override
    public String generateJwtToken(final String username, final String[] roles) {
        return Jwts.builder()
                .subject(username)
                .claims(Map.of("roles", roles))
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
            logger.error("Invalid JWT token: {" + e.getMessage() + "}");
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
    }

    private Date getExpiresDate() {
        return Date.from(LocalDateTime
                .now().plusMinutes(this.jwtExpirationMin)
                .atZone(ZoneId.systemDefault()).toInstant());
    }
}
