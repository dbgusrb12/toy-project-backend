package com.hg.blog.util;

import com.hg.blog.domain.account.entity.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTProvider {

    private static final String JWT_TOKEN_KEY = "secretKey";
    private static final int JWT_EXPIRED_MS = 604800000;

    // jwt 토큰 생성
    public static String generateToken(Account account) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRED_MS);

        return Jwts.builder()
            .setSubject(account.getUserId())
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, JWT_TOKEN_KEY)
            .compact();
    }

    public static String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(JWT_TOKEN_KEY)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_TOKEN_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}
