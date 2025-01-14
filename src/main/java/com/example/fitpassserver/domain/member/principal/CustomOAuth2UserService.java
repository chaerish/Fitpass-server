package com.example.fitpassserver.domain.member.principal;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.oauth.OAuthAttributes;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getProviderId(),
                createdMember.getId(),
                createdMember.getRole()
        );
    }

    private Member getMember(OAuthAttributes attributes) {
        Optional<Member> findMember = memberRepository.findByProviderId(attributes.getOauth2UserInfo().getProviderId());

        if (findMember.isEmpty()) {
            Member newMember = saveMember(attributes);
            newMember.updateIsAdditionInfo(true);
            return newMember;
        }
        return findMember.get();
    }

    private Member saveMember(OAuthAttributes attributes) {
        Member createdMember = attributes.toMember(attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);
    }
}
