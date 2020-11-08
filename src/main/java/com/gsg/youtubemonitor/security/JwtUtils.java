package com.gsg.youtubemonitor.security;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtils {

    private final int expireMinutes;

    private final int refreshExpireDays;

    private final String key;

    private final String refreshKey;

    public JwtUtils(@Value("${com.gsg.jwt.expireMinutes}") int expireMinutes,
                    @Value("${com.gsg.jwt.refreshExpireDays}") int refreshExpireDays,
                    @Value("${com.gsg.jwt.key}") String key,
                    @Value("${com.gsg.jwt.refreshKey}") String refreshKey) {
        this.expireMinutes = expireMinutes;
        this.refreshExpireDays = refreshExpireDays;
        this.key = key;
        this.refreshKey = refreshKey;
    }

    public String generateJwt(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(getCurrentDate())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String generateRefreshJwt(String username) {
        return Jwts.builder()
                .setSubject(username + ":refresh")
                .setIssuedAt(getCurrentDate())
                .setExpiration(getRefreshExpirationDate())
                .signWith(SignatureAlgorithm.HS256, refreshKey)
                .compact();
    }


    public boolean validate(String jwtToken, String username) {
        String jwtUsername = getUserName(jwtToken);
        return username.equals(jwtUsername);
    }

    public String getUserName(String jwtToken) {
        Claims claims = getClaimsFromToken(jwtToken, key);
        if (claims == null) {
            return null;
        }
        return claims.getSubject();
    }

    public String getTokenFromRefreshToken(String refreshToken) throws YMException {
        Claims claims = getClaimsFromToken(refreshToken, refreshKey);
        boolean invalidRefreshToken = claims == null
                || claims.getSubject().split(":").length != 2
                || !claims.getSubject().split(":")[1].equals("refresh");
        if (invalidRefreshToken) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, "Invalid refresh token");
        }
        String username = claims.getSubject().split(":")[0];
        return generateJwt(username);
    }

    private Claims getClaimsFromToken(String jwtToken, String key) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    private Date getRefreshExpirationDate() {
        Calendar now = Calendar.getInstance();
        now.setTime(getCurrentDate());
        now.add(Calendar.DAY_OF_MONTH, refreshExpireDays);
        return now.getTime();
    }

    private Date getExpirationDate() {
        Calendar now = Calendar.getInstance();
        now.setTime(getCurrentDate());
        now.add(Calendar.MINUTE, expireMinutes);
        return now.getTime();
    }

    Date getCurrentDate() {
        return new Date();
    }
}
