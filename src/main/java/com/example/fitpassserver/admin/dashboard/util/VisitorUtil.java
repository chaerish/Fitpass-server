package com.example.fitpassserver.admin.dashboard.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class VisitorUtil {
    private final Set<String> loginUser = new HashSet<>();

    public void addVisitor(String username) {
        loginUser.add(username);
    }

    public void clear() {
        loginUser.clear();
    }

    public int getVisitCount() {
        return loginUser.size();
    }
}
