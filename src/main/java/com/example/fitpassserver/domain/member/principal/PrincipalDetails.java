package com.example.fitpassserver.domain.member.principal;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {
    private final Member member;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        Role role=member.getRole();
        roles.add(role.name());
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
