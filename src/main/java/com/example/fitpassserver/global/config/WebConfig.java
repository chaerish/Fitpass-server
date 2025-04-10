package com.example.fitpassserver.global.config;

import com.example.fitpassserver.domain.member.annotation.resolver.CurrentMemberResolver;
import com.example.fitpassserver.global.config.interceptor.VisitorInterceptor;
import com.example.fitpassserver.owner.owner.annotation.resolver.CurrentOwnerResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentMemberResolver authenticatedMemberResolver;
    private final CurrentOwnerResolver authenticatedOwnerResolver;
    private final VisitorInterceptor visitorInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberResolver);
        resolvers.add(authenticatedOwnerResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitorInterceptor);
    }
}
