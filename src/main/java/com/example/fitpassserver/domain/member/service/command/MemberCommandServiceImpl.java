package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    /** 회원가입 **/
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
    /** 로그인 **/
    @Override
    public MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.LoginDTO dto) {
        Member member = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 탈퇴 상태 확인
        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new MemberException(MemberErrorCode.INACTIVE_ACCOUNT);
        }

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
        }

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(member))
                .refreshToken(jwtProvider.createRefreshToken(member))
                .build();
    }
}
