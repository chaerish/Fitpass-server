package com.example.fitpassserver.global.oauth;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.global.oauth.provider.GoogleOAuth2UserInfo;
import com.example.fitpassserver.global.oauth.provider.OAuth2UserInfo;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oauth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public Member toMember(OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .provider("google")
                .providerId(oauth2UserInfo.getProviderId())
                .loginId(oauth2UserInfo.getProvider() + "_" + oauth2UserInfo.getProviderId())
                .name(oauth2UserInfo.getName())
                .password(UUID.randomUUID().toString())
                .phoneNumber("temp")
                .role(Role.GUEST)
                .isAgree(false)
                .isTermsAgreed(false)
                .isLocationAgreed(false)
                .isThirdPartyAgreed(false)
                .isMarketingAgreed(false)
                .build();
    }
}
