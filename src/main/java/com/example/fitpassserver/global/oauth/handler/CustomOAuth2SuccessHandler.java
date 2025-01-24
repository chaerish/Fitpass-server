package com.example.fitpassserver.global.oauth.handler;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.principal.CustomOAuth2User;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String REDIRECT_URL = "http://localhost:5173";

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

        // JWT 생성
        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("refreshToken", refreshToken);

        if (!member.isAdditionalInfo()) {
            response.setHeader("status", "register");
            response.sendRedirect(REDIRECT_URL + "/signup/step2");
        } else {
            response.setHeader("status", "home");
            response.sendRedirect(REDIRECT_URL);
        }
    }
}
