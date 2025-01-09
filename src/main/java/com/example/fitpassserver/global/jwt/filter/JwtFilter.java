package com.example.fitpassserver.global.jwt.filter;


import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.principal.PrincipalDetailsService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 구현할 부분
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.split(" ")[1];
                String loginId = jwtProvider.getLoginId(token);
                UserDetails userDetails = principalDetailsService.loadUserByUsername(loginId);

                if (userDetails != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MemberException(MemberErrorCode.NOT_FOUND);
                }
            }

            filterChain.doFilter(request, response);
        } catch (GeneralException e) {
            BaseErrorCode code = e.getBaseErrorCode();
            response.setStatus(code.getReasonHttpStatus().getHttpStatus().value());
            response.setContentType("application/json; charset=UTF-8");

            ApiResponse<Object> customResponse = ApiResponse.onFailure( code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), "");

            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getOutputStream(), customResponse);

        }
    }
}