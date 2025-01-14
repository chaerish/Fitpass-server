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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public CustomOAuth2SuccessHandler(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
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

        // 쿠키 설정
        addCookie(response, "accessToken", accessToken, 60 * 60 * 24); // 1일
        addCookie(response, "refreshToken", refreshToken, 60 * 60 * 24); // 1일

        if (!member.isAdditionalInfo()) {
            addCookie(response, "status", "additionalInfo", 60 * 5); // 상태 쿠키 설정 (5분)
            response.sendRedirect("http://localhost:3000/additional-info");
        } else {
            addCookie(response, "status", "home", 60 * 5); // 상태 쿠키 설정 (5분)
            response.sendRedirect("http://localhost:3000");
        }
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // JavaScript로 접근 불가
        cookie.setSecure(true); // HTTPS에서만 사용
        cookie.setPath("/"); // 모든 경로에 적용
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
