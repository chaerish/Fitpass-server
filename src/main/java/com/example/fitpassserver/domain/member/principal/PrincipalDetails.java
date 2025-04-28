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
    private final String loginId;
    private final String password;
    private final String role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    @Override
    public String getUsername() {
        return loginId;
    }
    @Override
    public String getPassword() {
        return password;
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
