package com.example.fitpassserver.global.config.interceptor;

import com.example.fitpassserver.admin.dashboard.util.VisitorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class VisitorInterceptor implements HandlerInterceptor {

    private final VisitorUtil visitorUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = request.getRemoteUser();
        if (username != null) {
            visitorUtil.addVisitor(username);
        }
        return true;
    }
}
