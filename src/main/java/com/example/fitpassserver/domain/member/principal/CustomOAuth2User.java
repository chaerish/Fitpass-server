package com.example.fitpassserver.domain.member.principal;

import com.example.fitpassserver.domain.member.entity.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String providerId;
    private final Long id;
    private final Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String providerId, Long id, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.providerId = providerId;
        this.id = id;
        this.role = role;
    }
}