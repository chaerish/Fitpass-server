package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.member.sms.repositroy.SmsRepository;
import com.example.fitpassserver.domain.member.sms.service.SmsService;
import com.example.fitpassserver.domain.profile.entity.Profile;
import com.example.fitpassserver.domain.profile.repositroy.ProfileRepository;
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
    private final ProfileRepository profileRepository;
    private final SmsService smsService;
    private final SmsRepository smsRepository;

    /**
     * 회원가입
     **/
    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDTO request) {
        //이미 가입된 번호인지 확인
        if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_PHONE_NUMBER);
        }
        //필수 동의 사항 검증
        if (!request.isTermsAgreed() || !request.isLocationAgreed() || !request.isThirdPartyAgreed()) {
            throw new MemberException(MemberErrorCode.BAD_REQUEST);
        }

        Member newMember = MemberConverter.toMember(request);

        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));

        Member savedMember = memberRepository.save(newMember);

        //profile 객체 생성
        Profile profile = Profile.builder()
                .member(savedMember)
                .pictureKey("none")  //기본 프로필
                .build();

        profileRepository.save(profile);

        return savedMember;
    }

    /**
     * 로그인
     **/
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

    /**
     * 토큰 재발급
     */
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

    /**
     * 회원 탈퇴(soft delete)
     **/
    @Transactional
    @Override
    public void deactivateAccount(Member member) {
        Member currentMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND)); // 회원이 없으면 예외 발생

        currentMember.deactivateAccount(); // 탈퇴 메서드 호출

        memberRepository.save(currentMember); // 상태 변경된 엔티티 저장
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

    /**
     * 전화번호 변경
     **/
    @Override
    @Transactional
    public void changePhoneNumber(Member member, MemberRequestDTO.ChangePhoneNumberDTO request) {
        String newPhoneNumber = request.getNewPhoneNumber();

        //전화번호 중복 확인은 인증번호 발송하며 확인
        //회원 조회
        Member currentMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        //비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
        }

        //레디스에서 인증 확인
        if (!smsRepository.hasKey(newPhoneNumber)) {
            throw new MemberException(MemberErrorCode.UNVERIFIED_PHONE_NUMBER);
        }

        member.updatePhoneNumber(request.getNewPhoneNumber());
        memberRepository.save(member);

        //레디스에서 해당 번호 삭제
        smsRepository.deleteSmsCertification(newPhoneNumber);

    }

    /**
     * 비밀번호 리셋(비밀번호 찾기 후)
     **/
    @Override
    @Transactional
    public void resetPassword(MemberRequestDTO.ResetPasswordDTO request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }

    /**
     * 비밀번호 변경
     **/
    @Override
    @Transactional
    public void changePassword(Member member, MemberRequestDTO.ChangePasswordDTO request) {

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
        }

        Member currentMember = memberRepository.findByLoginId(member.getLoginId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        currentMember.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        memberRepository.save(currentMember);
    }


}
