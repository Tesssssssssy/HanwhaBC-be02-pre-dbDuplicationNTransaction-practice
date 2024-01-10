package com.example.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
    public static String generateAccessToken(String username, Integer id, String key, int expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("id", id);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + expiredTimeMs)*1000))
                .signWith(getSignKey(key), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static Key getSignKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public static Boolean validate(String token, String key) {
//        String usernameByToken = getUsername(token, username);
        String usernameByToken = getUsername(token, key);

        Date expireTime = extractAllClaims(token, key).getExpiration();
        Boolean result = expireTime.before(new Date(System.currentTimeMillis()));

        return !result;
    }

    public static String getUsername(String token, String key) {
        return extractAllClaims(token, key).get("username", String.class);

    }
    public static Integer getUserId(String token, String key) {
        return extractAllClaims(token, key).get("id", Integer.class);

    }

    public static Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
