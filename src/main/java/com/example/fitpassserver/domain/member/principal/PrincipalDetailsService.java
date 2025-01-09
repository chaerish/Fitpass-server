package com.example.fitpassserver.domain.member.principal;


import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    //loginId로 UserDetail 가져오기
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        return new PrincipalDetails(member);
    }
}
