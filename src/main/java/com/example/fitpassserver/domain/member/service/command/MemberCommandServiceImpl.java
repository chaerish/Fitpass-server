package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDTO request) {
        //필수 동의 사항 검증
        if (!request.isTermsAgreed() || !request.isLocationAgreed() || !request.isThirdPartyAgreed()) {
            throw new MemberException(MemberErrorCode.BAD_REQUEST);
        }

        Member newMember = MemberConverter.toMember(request);

        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));

        return memberRepository.save(newMember);
    }
}
