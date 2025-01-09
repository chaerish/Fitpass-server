package com.example.fitpassserver.global.config;


import com.example.fitpassserver.domain.member.principal.PrincipalDetailsService;
import com.example.fitpassserver.global.jwt.filter.JwtFilter;
import com.example.fitpassserver.global.jwt.handler.JwtAccessDeniedHandler;
import com.example.fitpassserver.global.jwt.handler.JwtAuthenticationEntryPoint;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final String[] allowUrl = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                //기본 폼 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                //기본 HTTP Basic 인증 비활성화
                .httpBasic(HttpBasicConfigurer::disable)
                //crsf 보안 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                //예외처리 설정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;
        return http.build();
    }
    @Bean
    public Filter jwtFilter() {
        return new JwtFilter(jwtProvider, principalDetailsService);
    }
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
