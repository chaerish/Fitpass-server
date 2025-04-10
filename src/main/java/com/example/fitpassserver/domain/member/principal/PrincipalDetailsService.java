package com.example.fitpassserver.domain.member.principal;


import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.common.support.LoginUser;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;

    //loginId로 UserDetail 가져오기
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // Member 또는 Owner 중 하나를 조회
        LoginUser loginUser = findLoginUserByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // UserDetails 객체 생성
        return org.springframework.security.core.userdetails.User
                .withUsername(loginUser.getLoginId())
                .password(loginUser.getPassword())
                .roles(loginUser.getRole().name())
                .build();
    }

    private Optional<LoginUser> findLoginUserByLoginId(String loginId) {
        // Member 조회 시도
        Optional<LoginUser> member = memberRepository.findByLoginId(loginId)
                .map(loginUser -> (LoginUser) loginUser);

        // 찾으면 Member 반환
        if (member.isPresent()) {
            return member;
        }

        // 아니면 Owner 조회 시도
        return ownerRepository.findByLoginId(loginId)
                .map(loginUser -> (LoginUser) loginUser);
    }
}
