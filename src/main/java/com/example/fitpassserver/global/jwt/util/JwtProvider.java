package com.example.fitpassserver.global.jwt.util;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.jwt.exception.AuthException;
import com.example.fitpassserver.global.jwt.exception.JwtErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private SecretKey secret;
    private long accessExpiration;
    private long refreshExpiration;

    public JwtProvider(@Value("${Jwt.secret}") String secret, @Value("${Jwt.token.access-expiration-time}") long accessExpiration, @Value("${Jwt.token.refresh-expiration-time}") long refreshExpiration) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String createAccessToken(Member member) {
        return createToken(member, this.accessExpiration);
    }

    public String createRefreshToken(Member member) {
        return createToken(member, this.refreshExpiration);
    }
    //공통 토큰 생성
    public String createToken(Member member, long expiration) {
        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plusMillis(expiration);
        return Jwts.builder()
                .setHeader(Map.of("alg", "HS256", "typ", "JWT"))
                .setSubject(member.getPhoneNumber())
                .claim("id", member.getId())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }
    //토큰 유효성 확인
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            return claims.getBody().getExpiration().after(Date.from(Instant.now()));
        } catch (JwtException e) {
            log.error(e.getMessage());
            return false;
        } catch (Exception e) {
            log.error(e.getMessage() + ": 토큰이 유효하지 않습니다.");
            return false;
        }
    }
    //토큰의 클레임 가져오는 메서드
    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new AuthException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    //loginId 추출
    public String getLoginId(String token) {
        return getClaims(token).getBody().getSubject();
    }

}
