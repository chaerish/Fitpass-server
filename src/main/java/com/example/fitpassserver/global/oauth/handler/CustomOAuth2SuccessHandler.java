package com.example.fitpassserver.global.oauth.handler;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.principal.CustomOAuth2User;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final long accessExpiration;
    private final long refreshExpiration;

    @Value("${spring.security.oauth2.redirect-url}")
    private String REDIRECT_URL;
    @Value("${property.name}")
    private String SPRING_PROFILE;

    public CustomOAuth2SuccessHandler(JwtProvider jwtProvider, MemberRepository memberRepository, @Value("${Jwt.token.access-expiration-time}") long accessExpiration, @Value("${Jwt.token.refresh-expiration-time}") long refreshExpiration) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Member member = memberRepository.findById(oAuth2User.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        member.updateLastLoginAt(LocalDateTime.now());

        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);
        String memberRole = member.getRole().toString();

        addCookie(response, "accessToken", accessToken, (int) (accessExpiration / 1000));
        addCookie(response, "refreshToken", refreshToken, (int) (refreshExpiration / 1000));
        addCookie(response, "memberRole", memberRole, (int) (accessExpiration / 1000));

        if (!member.isAdditionalInfo()) {
            addCookie(response, "status", "register", 60 * 5);
            response.sendRedirect(REDIRECT_URL + "/signup/step2");
        } else {
            addCookie(response, "status", "home", 60 * 5);
            response.sendRedirect(REDIRECT_URL);
        }
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .maxAge(maxAge);

        if (SPRING_PROFILE.equals("prod")) {
            cookieBuilder.domain(".fitpass.co.kr");
        }

        response.addHeader("Set-Cookie", cookieBuilder.build().toString());
    }
}
