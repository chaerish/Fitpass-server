package com.example.fitpassserver.domain.member.service.query;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.member.sms.repositroy.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;
    private final SmsRepository smsRepository;

    @Override
    public Member getMember(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }

    @Override
    public boolean checkLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new MemberException(MemberErrorCode.DUPLICATE_LOGINID);
        }
        return true;
    }

    @Override
    public String getLoginId(MemberRequestDTO.FindLoginIdDTO requst) {
        String name = requst.getName();
        String phoneNumber = requst.getPhoneNumber();

        //이름 정보 확인
        if (!memberRepository.existsByName(name)) {
            throw new MemberException(MemberErrorCode.INVALID_INFO);
        }

        //전화번호 정보 확인
        if (!memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new MemberException(MemberErrorCode.INVALID_INFO);
        }

        //redis에서 인증여부 확인
        if (!smsRepository.hasKey(phoneNumber)) {
            throw new MemberException(MemberErrorCode.UNVERIFIED_PHONE_NUMBER);
        }

        Member member = memberRepository.findByNameAndPhoneNumber(name, phoneNumber)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        return member.getLoginId();
    }

    @Override
    public String findPassword(MemberRequestDTO.FindPasswordDTO requst) {
        String loginId = requst.getLoginId();
        String name = requst.getName();
        String phoneNumber = requst.getPhoneNumber();
        //로그인아이디 정보 확인
        if (!memberRepository.existsByLoginId(loginId)) {
            throw new MemberException(MemberErrorCode.INVALID_INFO);
        }
        //이름 정보 확인
        if (!memberRepository.existsByName(name)) {
            throw new MemberException(MemberErrorCode.INVALID_INFO);
        }

        //전화번호 정보 확인
        if (!memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new MemberException(MemberErrorCode.INVALID_INFO);
        }

        //redis에서 인증여부 확인
        if (!smsRepository.hasKey(phoneNumber)) {
            throw new MemberException(MemberErrorCode.UNVERIFIED_PHONE_NUMBER);
        }

        return loginId;
    }
}