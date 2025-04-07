package com.example.fitpassserver.global.common.support;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberLoginUserFinder implements LoginUserFinder {
    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    @Override
    public Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber) {
        return memberRepository.findByNameAndPhoneNumber(name, phoneNumber);
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByName(String name) {
        return memberRepository.existsByName(name);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return memberRepository.existsByPhoneNumber(phoneNumber);
    }
}
