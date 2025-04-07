package com.example.fitpassserver.global.common.service;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.member.sms.repositroy.SmsRepository;
import com.example.fitpassserver.global.common.dto.CommonRequestDTO;
import com.example.fitpassserver.global.common.dto.CommonResponseDTO;
import com.example.fitpassserver.global.common.support.LoginUser;
import com.example.fitpassserver.global.common.support.LoginUserFinder;
import com.example.fitpassserver.global.jwt.util.JwtProvider;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;
    private final List<LoginUserFinder> loginUserFinders;
    private final JwtProvider jwtProvider;
    private final SmsRepository smsRepository;

    /**
     * 로그인
     **/
    @Override
    @Transactional
    public CommonResponseDTO.MemberTokenDTO login(CommonRequestDTO.LoginDTO dto) {
        LoginUser loginUser = loginUserFinders.stream()
                .map(finder -> finder.findByLoginId(dto.getLoginId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));


        if (loginUser instanceof Member member) {
            if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
                throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
            }

            member.updateLastLoginAt(LocalDateTime.now());

            return CommonResponseDTO.MemberTokenDTO.builder()
                    .role(member.getRole())
                    .isLocationAgreed(member.isLocationAgreed())
                    .accessToken(jwtProvider.createAccessToken(member))
                    .refreshToken(jwtProvider.createRefreshToken(member))
                    .build();

        } else if (loginUser instanceof Owner owner) {
            if (!passwordEncoder.matches(dto.getPassword(), owner.getPassword())) {
                throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
            }

            return CommonResponseDTO.MemberTokenDTO.builder()
                    .role(owner.getRole())
                    .isLocationAgreed(false)
                    .accessToken(jwtProvider.createAccessToken(owner))
                    .refreshToken(jwtProvider.createRefreshToken(owner))
                    .build();

        } else {
            throw new MemberException(MemberErrorCode.UNSUPPORTED_USER_TYPE);
        }
    }

    /**
     * 토큰 재발급
     */
    @Override
    public CommonResponseDTO.MemberTokenDTO refreshToken(String refreshToken) {
        // Refresh Token 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        String tokenLoginId = jwtProvider.getLoginId(refreshToken);

        LoginUser loginUser = loginUserFinders.stream()
                .map(finder -> finder.findByLoginId(tokenLoginId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 새로운 토큰 생성
        String newAccessToken = jwtProvider.createAccessToken(loginUser);
        String newRefreshToken = jwtProvider.createRefreshToken(loginUser);

        return CommonResponseDTO.MemberTokenDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * 중복 아이디 확인
     **/
    @Override
    public boolean checkLoginId(String loginId) {
        boolean isDuplicate = loginUserFinders.stream()
                .map(finder -> finder.findByLoginId(loginId))
                .anyMatch(Optional::isPresent);

        if (isDuplicate) {
            throw new MemberException(MemberErrorCode.DUPLICATE_LOGINID);
        }

        return true;
    }

    /**
     * 아이디 찾기
     **/
    @Override
    public String getLoginId(MemberRequestDTO.FindLoginIdDTO requst) {
        String name = requst.getName();
        String phoneNumber = requst.getPhoneNumber();

        //redis에서 인증여부 확인
        if (!smsRepository.hasKey(phoneNumber)) {
            throw new MemberException(MemberErrorCode.UNVERIFIED_PHONE_NUMBER);
        }

        return loginUserFinders.stream()
                .map(finder -> finder.findByNameAndPhoneNumber(name, phoneNumber)) // 각 Finder에서 Optional로 조회
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(LoginUser::getLoginId)
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

    }

    /**
     * 비밀번호 찾기
     **/
    @Override
    public String findPassword(MemberRequestDTO.FindPasswordDTO requst) {
        String loginId = requst.getLoginId();
        String name = requst.getName();
        String phoneNumber = requst.getPhoneNumber();

        //redis에서 인증여부 확인
        if (!smsRepository.hasKey(phoneNumber)) {
            throw new MemberException(MemberErrorCode.UNVERIFIED_PHONE_NUMBER);
        }

        return loginUserFinders.stream()
                .map(finder -> finder.findByLoginId(loginId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(user -> user.getName().equals(name) && user.getPhoneNumber().equals(phoneNumber))
                .map(LoginUser::getLoginId)
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_INFO));
    }

    /**
     * 비밀번호 리셋
     **/
    @Override
    @Transactional
    public void resetPassword(MemberRequestDTO.ResetPasswordDTO request) {
        String loginId = request.getLoginId();
        String newPassword = request.getNewPassword();

        LoginUser loginUser = loginUserFinders.stream()
                .map(finder -> finder.findByLoginId(loginId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        if (loginUser instanceof Member member) {
            member.updatePassword(passwordEncoder.encode(newPassword));
        } else if (loginUser instanceof Owner owner) {
            owner.updatePassword(passwordEncoder.encode(newPassword));
        } else {
            throw new MemberException(MemberErrorCode.UNSUPPORTED_USER_TYPE);
        }
    }


}
