package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO.SocialJoinDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO.MemberTokenDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.principal.CustomOAuth2User;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    /** 토큰 재발급 */
    @Override
    public MemberResponseDTO.MemberTokenDTO refreshToken(String refreshToken) {
        // Refresh Token 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        // Refresh Token에서 이메일 추출
        String tokenLoginId = jwtProvider.getLoginId(refreshToken);

        // 해당 이메일의 회원 정보 가져오기
        Member member = memberRepository.findByLoginId(tokenLoginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 새로운 토큰 생성
        String newAccessToken = jwtProvider.createAccessToken(member);
        String newRefreshToken = jwtProvider.createRefreshToken(member);

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /** 회원 탈퇴(soft delete) **/
    @Transactional
    @Override
    public void deactivateAccount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND)); // 회원이 없으면 예외 발생

        member.deactivateAccount(); // 탈퇴 메서드 호출

        memberRepository.save(member); // 상태 변경된 엔티티 저장
    }

    @Override
    @Transactional
    public Member socialJoinMember(MemberRequestDTO.SocialJoinDTO request, String accessToken) {
        String loginId = jwtProvider.getLoginId(accessToken);
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        member.socialJoin(request);
        member.updateRole(Role.MEMBER);
        member.updateIsAdditionInfo(true);

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public void setLocation(String loginId, MemberRequestDTO.LocationDTO dto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        member.setLocation(dto.getLatitude(), dto.getLongitude());
    }
}
