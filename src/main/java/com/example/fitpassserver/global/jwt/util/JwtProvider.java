package com.example.fitpassserver.global.jwt.util;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.global.common.support.LoginUser;
import com.example.fitpassserver.global.common.support.LoginUserFinder;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class JwtProvider {

    private final List<LoginUserFinder> loginUserFinders;
    private final SecretKey secret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtProvider(List<LoginUserFinder> loginUserFinders,
                       @Value("${Jwt.secret}") String secret,
                       @Value("${Jwt.token.access-expiration-time}") long accessExpiration,
                       @Value("${Jwt.token.refresh-expiration-time}") long refreshExpiration) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.loginUserFinders = loginUserFinders;
    }

    public String createAccessToken(LoginUser user) {
        return createToken(user, accessExpiration);
    }

    public String createRefreshToken(LoginUser user) {
        return createToken(user, refreshExpiration);
    }

    private String createToken(LoginUser user, long expiration) {
        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plusMillis(expiration);

        return Jwts.builder()
                .setHeader(Map.of("alg", "HS256", "typ", "JWT"))
                .setSubject(user.getLoginId())
                .claim("id", user.getId())
                .claim("role", user.getRole().name()) // optional: 권한 정보 추가
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiredAt))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

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

    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                    .clockSkewSeconds(60)
                    .verifyWith(secret)
                    .build().parseSignedClaims(token);
        } catch (JwtException e) {
            log.error("JWT {} 오류: {}", e.getClass(), e.getMessage());
            throw new AuthException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    public String getLoginId(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            String loginId = claims.getPayload().getSubject();

            LoginUser user = loginUserFinders.stream()
                    .map(finder -> finder.findByLoginId(loginId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

            if (user instanceof Member member) {
                if (member.getStatus() == MemberStatus.INACTIVE) {
                    throw new MemberException(MemberErrorCode.INACTIVE_ACCOUNT);
                }
            }

            return !claims.getPayload().getExpiration().before(new Date());
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}

