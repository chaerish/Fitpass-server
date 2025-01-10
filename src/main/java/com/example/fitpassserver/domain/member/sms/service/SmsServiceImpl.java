package com.example.fitpassserver.domain.member.sms.service;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.member.sms.dto.SmsRequestDTO;
import com.example.fitpassserver.domain.member.sms.repositroy.SmsRepository;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsCertificationUtil smsCertificationUtil; // SMS 인증 유틸리티 객체
    private final SmsRepository smsRepository; // SMS 레포지토리 객체 (Redis)
    private final MemberRepository memberRepository;


    @Override // SmsService 인터페이스의 메서드를 구현
    public void SendSms(SmsRequestDTO.CodeSendDTO smsRequestDto) {
        String phoneNum = smsRequestDto.getPhoneNumber(); // SmsRequestDTO에서 전화번호를 가져옴

        //이미 가입된 번호인지 확인
        Member member =memberRepository.findByPhoneNumber(phoneNum).orElse(null);

        if (member != null) {
            throw new MemberException(MemberErrorCode.DUPLICATE_PHONE_NUMBER);
        }

        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드를 랜덤으로 생성
        smsCertificationUtil.sendSMS(phoneNum, certificationCode); // SMS 인증 유틸리티를 사용하여 SMS 발송
        smsRepository.createSmsCertification(phoneNum, certificationCode); // 인증 코드를 Redis에 저장
    }

    @Override // SmsService 인터페이스의 메서드를 구현
    public boolean verifyCode(SmsRequestDTO.CodeVertifyDTO smsVerifyDto) {
        if (isVerify(smsVerifyDto.getPhoneNumber(), smsVerifyDto.getCertificationCode())) { // 인증 코드 검증
            smsRepository.deleteSmsCertification(smsVerifyDto.getPhoneNumber()); // 검증이 성공하면 Redis에서 인증 코드 삭제
            return true; // 인증 성공 반환
        } else {
            return false; // 인증 실패 반환
        }
    }

    // 전화번호와 인증 코드를 검증하는 메서드
    public boolean isVerify(String phoneNum, String certificationCode) {
        return smsRepository.hasKey(phoneNum) && // 전화번호에 대한 키가 존재하고
                smsRepository.getSmsCertification(phoneNum).equals(certificationCode); // 저장된 인증 코드와 입력된 인증 코드가 일치하는지 확인
    }
}
