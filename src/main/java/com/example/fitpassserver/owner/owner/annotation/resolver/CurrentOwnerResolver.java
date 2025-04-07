package com.example.fitpassserver.owner.owner.annotation.resolver;

import com.example.fitpassserver.global.jwt.util.JwtProvider;
import com.example.fitpassserver.owner.owner.annotation.CurrentOwner;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.service.query.OwnerQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrentOwnerResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final OwnerQueryService ownerQueryService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentOwner.class)
                && parameter.getParameterType().isAssignableFrom(Owner.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String header = webRequest.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // "Bearer " 제거
            String loginId = jwtProvider.getLoginId(token);
            return ownerQueryService.getOwner(loginId);
        }

        return null;
    }
}
